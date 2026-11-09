package com.taotao.cloud.mq.consistency.ttc.broker.transaction;

import com.taotao.cloud.mq.consistency.ttc.broker.transaction.TransactionStateManager;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 事务回查调度服务（对标 RocketMQ TransactionCheckService）.
 * <p>
 * 定期扫描待回查的事务消息，向 Producer 发起回查请求：
 * <ul>
 *   <li>扫描 HALF 状态的事务记录</li>
 *   <li>超过回查间隔的记录触发回查</li>
 *   <li>超过最大回查次数的记录自动回滚</li>
 * </ul>
 */
public class TransactionCheckService {

    private static final Logger log = LoggerFactory.getLogger(TransactionCheckService.class);

    /**
     * 把"回查得到的结论"兑现出去。
     * <p>
     * 为什么要有这一层：提交不是一次状态改写，它是"改写 + 把消息按原始 topic 回投 + 落一条定论记录"。
     * 这三步在 {@code TransactionMessageProcessor} 里已经有一份了，回查驱动再抄一份就是两个真相，
     * 早晚漂移。所以由 broker 装配时把处理器那一侧递进来，定时回查与客户端二次确认共用同一个兑现点。
     */
    public interface TransactionOutcomeApplier {
        void apply(String transactionId, com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState state);
    }

    private final TransactionStateManager transactionStateManager;
    private final TransactionCheckCallback checkCallback;
    private volatile TransactionOutcomeApplier outcomeApplier;
    private ScheduledExecutorService checkExecutor;
    private volatile boolean running = false;

    /**
     * 回查回调接口。
     */
    public interface TransactionCheckCallback {
        /**
         * 向 Producer 发起事务回查。
         *
         * @param transactionId 事务 ID
         * @param halfMessage   Half 消息
         * @return Producer 返回的事务状态
         */
        com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState checkTransaction(String transactionId, Message halfMessage);
    }

    public TransactionCheckService(TransactionStateManager transactionStateManager,
                                   TransactionCheckCallback checkCallback) {
        this.transactionStateManager = transactionStateManager;
        this.checkCallback = checkCallback;
    }

    /**
     * 启动回查服务。
     *
     * @param intervalMillis 回查扫描间隔（毫秒）
     */
    public void start(long intervalMillis) {
        if (running) {
            return;
        }
        this.checkExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "TransactionCheckThread");
            t.setDaemon(true);
            return t;
        });
        this.running = true;
        this.checkExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkOnce();
            }
        }, intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);
        log.info("TransactionCheckService started: interval={}ms", intervalMillis);
    }

    /**
     * 停止回查服务。
     */
    public void shutdown() {
        this.running = false;
        if (checkExecutor != null) {
            checkExecutor.shutdownNow();
        }
        log.info("TransactionCheckService shutdown");
    }

    /**
     * 扫一轮待回查事务。定时任务走的也是这一个方法。
     * <p>
     * 单列出来是为了让"这一轮到底改了什么状态"可以在没有定时等待的情况下被驱动与断言：
     * 判据落在状态变化上，而不是落在"睡一会儿之后大概扫过了"上。
     */
    public void checkOnce() {
        if (!running || checkCallback == null) {
            return;
        }

        try {
            List<TransactionStateManager.TransactionRecord> pendingList =
                    transactionStateManager.getPendingTransactions();

            for (TransactionStateManager.TransactionRecord record : pendingList) {
                if (record.getCheckTimes() >= transactionStateManager.getMaxCheckTimes()) {
                    // 超过最大回查次数，自动回滚
                    applyOutcome(record.getTransactionId(),
                            com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState.ROLLBACK);
                    log.warn("Transaction max check times reached, auto rolled back: transactionId={}",
                            record.getTransactionId());
                    continue;
                }

                try {
                    // 调用回调进行回查
                    com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState state =
                            checkCallback.checkTransaction(record.getTransactionId(), record.getHalfMessage());

                    switch (state) {
                        case COMMIT:
                            applyOutcome(record.getTransactionId(),
                                    com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState.COMMIT);
                            log.info("Transaction check result: COMMIT, transactionId={}", record.getTransactionId());
                            break;
                        case ROLLBACK:
                            applyOutcome(record.getTransactionId(),
                                    com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState.ROLLBACK);
                            log.info("Transaction check result: ROLLBACK, transactionId={}", record.getTransactionId());
                            break;
                        case UNKNOWN:
                        default:
                            // 继续等待下次回查
                            transactionStateManager.markChecked(record.getTransactionId());
                            log.debug("Transaction check result: UNKNOWN, transactionId={} checkTimes={}",
                                    record.getTransactionId(), record.getCheckTimes() + 1);
                            break;
                    }
                } catch (Exception e) {
                    log.error("Transaction check failed: transactionId={}", record.getTransactionId(), e);
                    transactionStateManager.markChecked(record.getTransactionId());
                }
            }

            // 清理过期事务
            int cleaned = transactionStateManager.cleanExpiredTransactions();
            if (cleaned > 0) {
                log.info("Cleaned {} expired transactions", cleaned);
            }
        } catch (Exception e) {
            log.error("Transaction check scan failed", e);
        }
    }

    /** 一次结论的全部副作用都走这一个出口（幂等：重复结论不会投第二份，见状态表的 resolved 表）。 */
    private void applyOutcome(String transactionId, com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState state) {
        TransactionOutcomeApplier applier = this.outcomeApplier;
        if (applier != null) {
            applier.apply(transactionId, state);
            return;
        }
        // 没有装配兑现点时退回"只改状态"这一半 —— 那也是本服务接线之前的全部行为。
        if (state == com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState.COMMIT) {
            transactionStateManager.commitTransaction(transactionId);
        } else {
            transactionStateManager.rollbackTransaction(transactionId);
        }
    }

    /** 由 broker 装配：回查结论要由谁来兑现（正常装配是事务处理器）。 */
    public void setTransactionOutcomeApplier(TransactionOutcomeApplier outcomeApplier) {
        this.outcomeApplier = outcomeApplier;
    }

    /**
     * 是否正在运行。
     */
    public boolean isRunning() {
        return running;
    }
}
