package com.taotao.cloud.mq.broker.transaction;

import com.taotao.cloud.mq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务状态管理器（对标 RocketMQ TransactionStateManager）.
 * <p>
 * 管理 Half 消息的事务状态：
 * <ul>
 *   <li>存储 Half 消息和事务状态</li>
 *   <li>支持 COMMIT（提交）和 ROLLBACK（回滚）操作</li>
 *   <li>提供待回查消息查询能力</li>
 * </ul>
 * <p>
 * 事务消息流程：
 * <ol>
 *   <li>Producer 发送 Half 消息 → Broker 存储到此管理器</li>
 *   <li>Producer 发送 COMMIT/ROLLBACK → Broker 更新状态</li>
 *   <li>超时未确认 → Broker 发起回查</li>
 * </ol>
 */
public class TransactionStateManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionStateManager.class);

    /** 事务状态枚举 */
    public enum TxState {
        /** Half 消息已存储，等待二次确认 */
        HALF,
        /** 已提交，消息对消费者可见 */
        COMMITTED,
        /** 已回滚，消息丢弃 */
        ROLLBACKED,
        /** 等待回查 */
        CHECKING
    }

    /**
     * 一次二次确认的结论。把"这次确认到底改变了什么"表达成一个值，
     * 调用方（处理器 → 客户端）才能区分"提交成功"和"这条早就定过了"。
     */
    public enum ResolveOutcome {
        /** 本次把 HALF/CHECKING 改成了 COMMITTED */
        COMMITTED,
        /** 本次把 HALF/CHECKING 改成了 ROLLBACKED */
        ROLLBACKED,
        /** 之前已经提交过（幂等：不再回投第二份） */
        ALREADY_COMMITTED,
        /** 之前已经回滚过（幂等：不会再被提交） */
        ALREADY_ROLLBACKED,
        /** 状态改成了提交，但消息回投没落住：这次提交不算成，结论已撤回，调用方可以重发 */
        DELIVERY_FAILED,
        /** 客户端答"本地事务还没结论"：这条仍留在待回查集合里等回查，不是错误也不是没找到 */
        STILL_PENDING,
        /** 这条事务不在表里（半消息没来过，或已过清理窗口） */
        NOT_FOUND
    }

    /**
     * 事务消息记录。
     */
    public static class TransactionRecord {
        private final String transactionId;
        private final String originalTopic;
        private final int originalQueueId;
        private final Message halfMessage;
        private TxState state;
        private int checkTimes;
        private long lastCheckTimestamp;
        private long createTimestamp;
        /** 半消息在 TRANS_HALF_TOPIC 上的队列位点；恢复时用它认"这条已经从盘上重建过了"。 */
        private long halfQueueOffset = -1L;

        public TransactionRecord(String transactionId, String originalTopic, Message halfMessage) {
            this(transactionId, originalTopic, 0, halfMessage);
        }

        public TransactionRecord(String transactionId, String originalTopic, int originalQueueId,
                                 Message halfMessage) {
            this.transactionId = transactionId;
            this.originalTopic = originalTopic;
            this.originalQueueId = originalQueueId;
            this.halfMessage = halfMessage;
            this.state = TxState.HALF;
            this.checkTimes = 0;
            this.lastCheckTimestamp = 0;
            this.createTimestamp = System.currentTimeMillis();
        }

        // Getters and Setters
        public String getTransactionId() { return transactionId; }
        public String getOriginalTopic() { return originalTopic; }
        public int getOriginalQueueId() { return originalQueueId; }
        public Message getHalfMessage() { return halfMessage; }
        public TxState getState() { return state; }
        public void setState(TxState state) { this.state = state; }
        public int getCheckTimes() { return checkTimes; }
        public void setCheckTimes(int checkTimes) { this.checkTimes = checkTimes; }
        public long getLastCheckTimestamp() { return lastCheckTimestamp; }
        public void setLastCheckTimestamp(long lastCheckTimestamp) { this.lastCheckTimestamp = lastCheckTimestamp; }
        public long getCreateTimestamp() { return createTimestamp; }
        public void setCreateTimestamp(long createTimestamp) { this.createTimestamp = createTimestamp; }
        public long getHalfQueueOffset() { return halfQueueOffset; }
        public void setHalfQueueOffset(long halfQueueOffset) { this.halfQueueOffset = halfQueueOffset; }

        public boolean isPending() {
            return state == TxState.HALF || state == TxState.CHECKING;
        }

        public boolean isExpired(long maxAgeMillis) {
            return System.currentTimeMillis() - createTimestamp > maxAgeMillis;
        }
    }

    /** 事务记录存储: transactionId -> TransactionRecord */
    private final ConcurrentHashMap<String, TransactionRecord> transactionStore = new ConcurrentHashMap<>();

    /**
     * 已经定论（提交/回滚）过的事务: transactionId -> 结论状态。
     * 这张表存在的唯一理由：二次确认可能重发（客户端补发、回查驱动、重启后重放），
     * 没有它就没法把"再提交一次"挡在回投之前——那会变成同一条消息投两遍。
     */
    private final ConcurrentHashMap<String, TxState> resolvedTable = new ConcurrentHashMap<>();

    /** 默认事务超时时间（15分钟） */
    private static final long DEFAULT_TX_TIMEOUT_MILLIS = 15 * 60 * 1000L;

    /** 默认最大回查次数 */
    private static final int DEFAULT_MAX_CHECK_TIMES = 15;

    /** 默认回查间隔（60秒） */
    private static final long DEFAULT_CHECK_INTERVAL_MILLIS = 60_000L;

    private long txTimeoutMillis = DEFAULT_TX_TIMEOUT_MILLIS;
    private int maxCheckTimes = DEFAULT_MAX_CHECK_TIMES;
    private long checkIntervalMillis = DEFAULT_CHECK_INTERVAL_MILLIS;

    /**
     * 存储 Half 消息。
     *
     * @param transactionId   事务 ID
     * @param originalTopic   原始 Topic（提交后消息回投到这里）
     * @param originalQueueId 原始队列（回投时保持同一条业务队列）
     * @param halfMessage     Half 消息
     */
    public void putHalfMessage(String transactionId, String originalTopic, int originalQueueId,
                               Message halfMessage) {
        TransactionRecord record = new TransactionRecord(transactionId, originalTopic, originalQueueId, halfMessage);
        transactionStore.put(transactionId, record);
        log.info("Half message stored: transactionId={} topic={} queueId={}",
                transactionId, originalTopic, originalQueueId);
    }

    /**
     * 存储 Half 消息（队列 0）。
     */
    public void putHalfMessage(String transactionId, String originalTopic, Message halfMessage) {
        putHalfMessage(transactionId, originalTopic, 0, halfMessage);
    }

    /**
     * 提交事务（COMMIT）。
     *
     * @param transactionId 事务 ID
     * @return 结论；只有 {@link ResolveOutcome#COMMITTED} 这一次需要回投消息
     */
    public ResolveOutcome commitTransaction(String transactionId) {
        return resolve(transactionId, TxState.COMMITTED);
    }

    /**
     * 回滚事务（ROLLBACK）。
     *
     * @param transactionId 事务 ID
     * @return 本次是否真的把一条待确认事务改成了回滚
     */
    public boolean rollbackTransaction(String transactionId) {
        return resolve(transactionId, TxState.ROLLBACKED) == ResolveOutcome.ROLLBACKED;
    }

    private ResolveOutcome resolve(String transactionId, TxState target) {
        TxState settled = resolvedTable.get(transactionId);
        if (settled != null) {
            log.warn("Transaction already resolved, ignore this one: transactionId={} settled={} requested={}",
                    transactionId, settled, target);
            return settled == TxState.COMMITTED ? ResolveOutcome.ALREADY_COMMITTED : ResolveOutcome.ALREADY_ROLLBACKED;
        }
        TransactionRecord record = transactionStore.get(transactionId);
        if (record == null) {
            log.warn("Transaction not found for resolve: transactionId={} target={}", transactionId, target);
            return ResolveOutcome.NOT_FOUND;
        }
        record.setState(target);
        resolvedTable.put(transactionId, target);
        log.info("Transaction resolved: transactionId={} target={} topic={}",
                transactionId, target, record.getOriginalTopic());
        return target == TxState.COMMITTED ? ResolveOutcome.COMMITTED : ResolveOutcome.ROLLBACKED;
    }

    /** 这条事务是否已经定论（提交或回滚）。 */
    public TxState getResolvedState(String transactionId) {
        return resolvedTable.get(transactionId);
    }

    /**
     * 撤销一次"已经定论但兑现失败"的结论。
     * <p>
     * 只给一种场景用：COMMIT 的状态已经改好了，但消息回投没落住 —— 这时"提交过了"是个假事实，
     * 留着它等于这条消息永远投不出去（后续的确认一律被幂等挡掉）。撤掉之后调用方可以重发确认。
     * 撤的同时把状态放回 HALF，这条事务重新变成"待确认"。
     */
    public void forgetResolveForRetry(String transactionId) {
        resolvedTable.remove(transactionId);
        TransactionRecord record = transactionStore.get(transactionId);
        if (record != null) {
            record.setState(TxState.HALF);
        }
        log.warn("Resolve rolled back in the state table because delivery failed: transactionId={}", transactionId);
    }

    /**
     * 从盘上重建出来一条半消息事务（broker 重启后恢复待回查集合用）。
     * <p>
     * 已经定论过的（提交/回滚过的事务）不会被收进来：调用方先按 op 记录把 resolved 集合喂进来，
     * 本方法再兜一层，避免"恢复顺序里半消息在 op 之前"时把已定论的事务又变回待回查。
     *
     * @return true 表示这条确实被收进待回查集合
     */
    public boolean recoverHalfMessage(String transactionId, String originalTopic, int originalQueueId,
                                      Message halfMessage, long halfQueueOffset, long createTimestamp) {
        if (transactionId == null || transactionId.isEmpty()) {
            return false;
        }
        if (resolvedTable.containsKey(transactionId)) {
            return false;
        }
        if (transactionStore.containsKey(transactionId)) {
            return false;
        }
        TransactionRecord record = new TransactionRecord(transactionId, originalTopic, originalQueueId, halfMessage);
        record.setHalfQueueOffset(halfQueueOffset);
        record.setCreateTimestamp(createTimestamp);
        transactionStore.put(transactionId, record);
        log.info("Half message recovered from store: transactionId={} topic={} queueId={} halfOffset={}",
                transactionId, originalTopic, originalQueueId, halfQueueOffset);
        return true;
    }

    /** 登记一条已经定论的事务（重启后从 op 记录重放出来）。 */
    public void recoverResolved(String transactionId, TxState state) {
        if (transactionId == null || transactionId.isEmpty() || state == null) {
            return;
        }
        resolvedTable.putIfAbsent(transactionId, state);
        TransactionRecord record = transactionStore.get(transactionId);
        if (record != null) {
            record.setState(state);
        }
    }

    /**
     * 获取所有待回查的事务记录（到了回查时间的那些）。
     *
     * @return 待回查的事务记录列表
     */
    public List<TransactionRecord> getPendingTransactions() {
        List<TransactionRecord> pending = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (TransactionRecord record : transactionStore.values()) {
            if (record.isPending() && !record.isExpired(txTimeoutMillis)) {
                // 检查是否到了回查时间
                if (now - record.getLastCheckTimestamp() >= checkIntervalMillis) {
                    pending.add(record);
                }
            }
        }
        return pending;
    }

    /**
     * 获取全部仍在待确认集合里的事务（不看回查间隔）。
     * <p>
     * 这一把是给"生产者主动来取待回查列表"用的：它问的是"现在有什么悬着"，不是"现在该催谁"。
     */
    public List<TransactionRecord> listAllPendingTransactions() {
        List<TransactionRecord> pending = new ArrayList<>();
        for (TransactionRecord record : transactionStore.values()) {
            if (record.isPending()) {
                pending.add(record);
            }
        }
        return pending;
    }

    /**
     * 更新回查次数和时间戳。
     *
     * @param transactionId 事务 ID
     */
    public void markChecked(String transactionId) {
        TransactionRecord record = transactionStore.get(transactionId);
        if (record != null) {
            record.setCheckTimes(record.getCheckTimes() + 1);
            record.setLastCheckTimestamp(System.currentTimeMillis());
            record.setState(TxState.CHECKING);
        }
    }

    /**
     * 获取事务记录。
     */
    public TransactionRecord getTransaction(String transactionId) {
        return transactionStore.get(transactionId);
    }

    /**
     * 清理已过期的事务记录。
     */
    public int cleanExpiredTransactions() {
        int cleaned = 0;
        for (Map.Entry<String, TransactionRecord> entry : transactionStore.entrySet()) {
            TransactionRecord record = entry.getValue();
            if (record.isExpired(txTimeoutMillis) && record.isPending()) {
                // 超时未确认，标记为回滚
                record.setState(TxState.ROLLBACKED);
                resolvedTable.put(entry.getKey(), TxState.ROLLBACKED);
                log.warn("Transaction expired and rolled back: transactionId={}", entry.getKey());
                cleaned++;
            }
        }
        return cleaned;
    }

    /**
     * 获取事务存储大小。
     */
    public int size() {
        return transactionStore.size();
    }

    /** 已定论（含从 op 记录重放出来的）条数。 */
    public int resolvedSize() {
        return resolvedTable.size();
    }

    // ==================== Getters and Setters ====================

    public long getTxTimeoutMillis() {
        return txTimeoutMillis;
    }

    public void setTxTimeoutMillis(long txTimeoutMillis) {
        this.txTimeoutMillis = txTimeoutMillis;
    }

    public int getMaxCheckTimes() {
        return maxCheckTimes;
    }

    public void setMaxCheckTimes(int maxCheckTimes) {
        this.maxCheckTimes = maxCheckTimes;
    }

    public long getCheckIntervalMillis() {
        return checkIntervalMillis;
    }

    public void setCheckIntervalMillis(long checkIntervalMillis) {
        this.checkIntervalMillis = checkIntervalMillis;
    }
}
