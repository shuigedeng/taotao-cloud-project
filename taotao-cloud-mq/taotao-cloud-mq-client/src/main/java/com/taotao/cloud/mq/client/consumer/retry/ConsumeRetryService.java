package com.taotao.cloud.mq.client.consumer.retry;

import com.taotao.cloud.mq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消费重试服务（对标 RocketMQ ConsumeRetryService）.
 * <p>
 * 管理消费失败消息的重试逻辑：
 * <ul>
 *   <li>消费失败时先尝试把这条消息<b>再往 broker 投一份</b>（可掉电不丢的那条通路）</li>
 *   <li>投递没成功时退回进程内队列，按重试策略（阶梯间隔/固定间隔）定时重新投递</li>
 *   <li>超过最大重试次数后转入死信队列（有发布器时同时真发到死信 Topic）</li>
 * </ul>
 * <p>
 * <b>重投次数的真相只有一份：{@link MessageExt#getReconsumeTimes()} 这个字段。</b>
 * 它是消息编解码里唯一跟着消息一起进 CommitLog、又能被读回来的那一列，
 * 所以"绕 broker 一圈之后次数还在"只能挂在它身上。计数写在这个字段上，
 * 属性表里只留"这条是重投消息"和"它原本死在哪个 Topic 上"这两条说明性信息。
 * <p>
 * 重试间隔与 RocketMQ 保持一致：
 * 无序消息阶梯间隔：10s → 30s → 1min → 2min → ... → 2h
 * 顺序消息固定间隔：可配置（默认 30s）
 */
public class ConsumeRetryService {

    private static final Logger log = LoggerFactory.getLogger(ConsumeRetryService.class);

    /** 默认最大重试次数 */
    public static final int DEFAULT_MAX_RECONSUME_TIMES = 16;

    /** 默认顺序消息固定重试间隔（30秒） */
    public static final long DEFAULT_ORDERLY_RETRY_INTERVAL_MILLIS = 30_000L;

    /** 默认的到期检查周期：调度线程每 1 秒扫一遍待重投的消息 */
    public static final long DEFAULT_RETRY_CHECK_INTERVAL_MILLIS = 1000L;

    /** 重投消息属性键：原始 Topic（死信副本靠它说明自己死在哪个 Topic 上） */
    public static final String PROPERTY_ORIGINAL_TOPIC = "RETRY_TOPIC";

    /** 重投消息属性键：这条消息是重投出来的（读侧是 {@link MessageExt#isRetry()}） */
    public static final String PROPERTY_RETRY = "RETRY";

    /** 重投消息属性键：重试次数——已废除，见类注释里的"唯一真相"那一段 */
    @Deprecated
    public static final String PROPERTY_RECONSUME_TIMES = "RECONSUME_TIMES";

    private final String consumerGroup;
    private final int maxReconsumeTimes;
    private final RetryPolicy retryPolicy;
    private final long fixedRetryIntervalMillis;
    private final long retryCheckIntervalMillis;
    private final RetryDelayStrategy delayStrategy;
    private final DeadLetterQueue deadLetterQueue;

    /** 重试消息存储：topic -> (msgId -> 待重试消息) */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, RetryMessage>> retryMessageStore = new ConcurrentHashMap<>();

    /** 重试调度器 */
    private ScheduledExecutorService retryExecutor;

    /** 重试回调接口 */
    private RetryCallback retryCallback;

    /** 落盘通路：把重投消息再往 broker 投一份；没设置时只有进程内这一条路 */
    private volatile RetryTransport retryTransport;

    /**
     * 重试消息包装类。
     */
    static class RetryMessage {
        final MessageExt message;
        final int reconsumeTimes;
        final long nextRetryTimestamp;
        final long originalBornTimestamp;

        RetryMessage(MessageExt message, int reconsumeTimes, long nextRetryTimestamp) {
            this.message = message;
            this.reconsumeTimes = reconsumeTimes;
            this.nextRetryTimestamp = nextRetryTimestamp;
            this.originalBornTimestamp = message.getBornTimestamp();
        }

        boolean isReady() {
            return System.currentTimeMillis() >= nextRetryTimestamp;
        }
    }

    /**
     * 重试回调接口。
     */
    public interface RetryCallback {
        /**
         * 重新投递消息进行消费。
         *
         * @param message 待重试的消息
         * @return 消费是否成功
         */
        boolean retryConsume(MessageExt message);
    }

    /**
     * 重投间隔策略：给出"第 reconsumeTimes 次重投"要等多久再投。
     * <p>
     * 生产默认就是既有的阶梯/固定两套数；换一个实现只是把"什么时候算到期"这件事
     * 交给调用方，不参与正确性判定。
     */
    public interface RetryDelayStrategy {
        /**
         * @param reconsumeTimes 即将执行的这一次重投的序号（从 1 开始）
         * @return 距离下次重投的延迟毫秒数
         */
        long nextDelayMillis(int reconsumeTimes);
    }

    /**
     * 重投消息的落盘通路：把这条消息（已经带上新的重投次数）再往 broker 投一份。
     * <p>
     * 返回 true 表示 broker 已经收下这份副本 —— 此时<b>不再</b>排进程内的定时重投，
     * 否则同一条消息会同时经由两条通路各投一次，级数每轮翻倍。
     * 返回 false（没设置通路、路由不通、发送失败）时退回进程内队列，语义与之前一致。
     */
    public interface RetryTransport {
        /**
         * @param message        待落盘的重投消息（其 reconsumeTimes 已更新为本次序号）
         * @param reconsumeTimes 本次重投的序号
         * @return 是否已经被 broker 收下
         */
        boolean sendRetryCopy(MessageExt message, int reconsumeTimes);
    }

    public ConsumeRetryService(String consumerGroup) {
        this(consumerGroup, DEFAULT_MAX_RECONSUME_TIMES, RetryPolicy.STEPPED, 0);
    }

    public ConsumeRetryService(String consumerGroup, int maxReconsumeTimes) {
        this(consumerGroup, maxReconsumeTimes, RetryPolicy.STEPPED, 0);
    }

    public ConsumeRetryService(String consumerGroup, int maxReconsumeTimes, RetryPolicy retryPolicy,
                               long fixedRetryIntervalMillis) {
        this(consumerGroup, maxReconsumeTimes, retryPolicy, fixedRetryIntervalMillis,
                DEFAULT_RETRY_CHECK_INTERVAL_MILLIS, null);
    }

    /**
     * @param retryCheckIntervalMillis 到期检查周期；生产默认 {@value #DEFAULT_RETRY_CHECK_INTERVAL_MILLIS} ms
     * @param delayStrategy            重投间隔策略；传 null 即沿用 retryPolicy/fixedRetryIntervalMillis 那两套既有数值
     */
    public ConsumeRetryService(String consumerGroup, int maxReconsumeTimes, RetryPolicy retryPolicy,
                               long fixedRetryIntervalMillis, long retryCheckIntervalMillis,
                               RetryDelayStrategy delayStrategy) {
        this.consumerGroup = consumerGroup;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.retryPolicy = retryPolicy;
        this.fixedRetryIntervalMillis = fixedRetryIntervalMillis;
        this.retryCheckIntervalMillis = retryCheckIntervalMillis > 0
                ? retryCheckIntervalMillis : DEFAULT_RETRY_CHECK_INTERVAL_MILLIS;
        this.delayStrategy = delayStrategy != null ? delayStrategy : new RetryDelayStrategy() {
            @Override
            public long nextDelayMillis(int reconsumeTimes) {
                if (retryPolicy == RetryPolicy.FIXED) {
                    return RetryPolicy.getFixedInterval(fixedRetryIntervalMillis);
                }
                return RetryPolicy.getSteppedInterval(reconsumeTimes);
            }
        };
        this.deadLetterQueue = new DeadLetterQueue(consumerGroup);
    }

    /**
     * 启动重试服务。
     */
    public void start() {
        this.retryExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ConsumeRetryThread_" + consumerGroup);
            t.setDaemon(true);
            return t;
        });
        // 每个检查周期扫一遍待重投队列
        this.retryExecutor.scheduleWithFixedDelay(this::processRetryQueue,
                retryCheckIntervalMillis, retryCheckIntervalMillis, TimeUnit.MILLISECONDS);
        log.info("ConsumeRetryService started: group={}, maxRetries={}, policy={}, checkIntervalMs={}",
                consumerGroup, maxReconsumeTimes, retryPolicy, retryCheckIntervalMillis);
    }

    /**
     * 停止重试服务。
     */
    public void shutdown() {
        if (retryExecutor != null) {
            retryExecutor.shutdownNow();
        }
        log.info("ConsumeRetryService shutdown: group={}", consumerGroup);
    }

    /**
     * 设置重试回调。
     */
    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    /**
     * 设置落盘通路（把重投消息再往 broker 投一份）。
     */
    public void setRetryTransport(RetryTransport retryTransport) {
        this.retryTransport = retryTransport;
    }

    /**
     * 消费失败的消息交给谁了 —— 调用方要靠这个读数决定位点动不动。
     * <ul>
     *   <li>{@link #OUTCOME_IN_PROCESS_RETRY}、{@link #OUTCOME_DEAD_LETTER_CACHED}：
     *       真相还在这个进程里，进程一停就没了 ⇒ 原来那条的位点<b>不许</b>推进，
     *       靠"未确认就被重新拉到"兜住。</li>
     *   <li>{@link #OUTCOME_RETRY_PERSISTED}、{@link #OUTCOME_DEAD_LETTER_PERSISTED}：
     *       已经有一份落到存储里的后继（重投副本或死信 Topic 里那条），这条消息的活由后继接手
     *       ⇒ 原来那条可以确认掉。不确认的代价不是重复，而是<b>指数级自我复制</b>：
     *       每被重新拉到一次就多落一个副本，副本又各自被重拉，第 N 级就有 2<sup>N</sup> 份。</li>
     * </ul>
     */
    public static final int OUTCOME_IN_PROCESS_RETRY = 1;
    public static final int OUTCOME_RETRY_PERSISTED = 2;
    public static final int OUTCOME_DEAD_LETTER_CACHED = 3;
    public static final int OUTCOME_DEAD_LETTER_PERSISTED = 4;

    /**
     * 消费失败时调用此方法：先试着把这条消息作为"第 reconsumeTimes + 1 次"的副本落盘，
     * 落不成再退回进程内重投队列。
     *
     * @param message        消费失败的消息
     * @param reconsumeTimes 这条消息<b>已经</b>重投过的次数（读自 {@link MessageExt#getReconsumeTimes()}）
     * @return true 表示已经排好下一次的投递（落盘成功，或已进进程内重投队列），
     *         false 表示次数已用尽（本轮转入死信队列）
     */
    public boolean addRetryMessage(MessageExt message, int reconsumeTimes) {
        int outcome = addRetryMessageForOutcome(message, reconsumeTimes);
        return outcome == OUTCOME_IN_PROCESS_RETRY || outcome == OUTCOME_RETRY_PERSISTED;
    }

    /**
     * 同 {@link #addRetryMessage(MessageExt, int)}，但把"交给谁了"说清楚，供调用方决定位点。
     *
     * @return 上面那五个 {@code OUTCOME_*} 之一
     */
    public int addRetryMessageForOutcome(MessageExt message, int reconsumeTimes) {
        if (reconsumeTimes >= maxReconsumeTimes) {
            // 次数用尽：这条消息的死活由死信队列负责，先打标记再交出去
            message.setReconsumeTimes(reconsumeTimes);
            stampRetryAttributes(message);
            boolean onDlqTopic = deadLetterQueue.putMessage(message, reconsumeTimes,
                    "Exceeded max reconsume times: " + maxReconsumeTimes);
            return onDlqTopic ? OUTCOME_DEAD_LETTER_PERSISTED : OUTCOME_DEAD_LETTER_CACHED;
        }

        int nextReconsumeTimes = reconsumeTimes + 1;
        // 唯一真相：次数记在字段上，跟着消息进存储；进程内那份只是本轮调度的读数
        message.setReconsumeTimes(nextReconsumeTimes);
        stampRetryAttributes(message);

        RetryTransport transport = this.retryTransport;
        if (transport != null) {
            boolean persisted;
            try {
                persisted = transport.sendRetryCopy(message, nextReconsumeTimes);
            } catch (Exception e) {
                log.warn("Retry copy not accepted by transport: group={} msgId={} err={}",
                        consumerGroup, message.getMsgId(), e.getMessage());
                persisted = false;
            }
            if (persisted) {
                // 副本已经在 broker 手里，下一次重投由"重新拉到这份副本"驱动；
                // 这里再排一次进程内定时投就是同一条消息投两遍。
                return OUTCOME_RETRY_PERSISTED;
            }
        }

        long nextRetryTimestamp = System.currentTimeMillis() + delayStrategy.nextDelayMillis(nextReconsumeTimes);
        RetryMessage retryMsg = new RetryMessage(message, nextReconsumeTimes, nextRetryTimestamp);

        // 按 Topic 分组存储
        retryMessageStore
                .computeIfAbsent(message.getTopic(), k -> new ConcurrentHashMap<>())
                .put(message.getMsgId(), retryMsg);

        log.debug("Message added to retry queue: topic={}, msgId={}, retryCount={}, nextRetryIn={}ms",
                message.getTopic(), message.getMsgId(), retryMsg.reconsumeTimes,
                retryMsg.nextRetryTimestamp - System.currentTimeMillis());
        return OUTCOME_IN_PROCESS_RETRY;
    }

    /**
     * 给要往外投的消息打标：它原本是哪个 Topic 的、它是重投出来的。
     * <p>
     * 次数不在这儿记 —— 见类注释里"唯一真相是字段"那一段。
     */
    private void stampRetryAttributes(MessageExt message) {
        if (message.getProperty(PROPERTY_ORIGINAL_TOPIC) == null) {
            message.putProperty(PROPERTY_ORIGINAL_TOPIC, message.getTopic());
        }
        message.putProperty(PROPERTY_RETRY, "true");
    }

    /**
     * 处理重试队列：检查到期消息并重新投递。
     */
    private void processRetryQueue() {
        if (retryCallback == null) {
            return;
        }

        for (Map.Entry<String, ConcurrentHashMap<String, RetryMessage>> topicEntry : retryMessageStore.entrySet()) {
            ConcurrentHashMap<String, RetryMessage> msgMap = topicEntry.getValue();
            Iterator<Map.Entry<String, RetryMessage>> iterator = msgMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, RetryMessage> entry = iterator.next();
                RetryMessage retryMsg = entry.getValue();

                if (retryMsg.isReady()) {
                    iterator.remove(); // 先移除，避免重复重试
                    try {
                        boolean success = retryCallback.retryConsume(retryMsg.message);
                        if (!success) {
                            // 重试消费失败，再次加入重试队列
                            addRetryMessage(retryMsg.message, retryMsg.reconsumeTimes);
                        }
                    } catch (Exception e) {
                        log.error("Retry consume failed: topic={}, msgId={}",
                                retryMsg.message.getTopic(), retryMsg.message.getMsgId(), e);
                        // 异常也加入重试队列
                        addRetryMessage(retryMsg.message, retryMsg.reconsumeTimes);
                    }
                }
            }
        }
    }

    /**
     * 获取死信队列。
     */
    public DeadLetterQueue getDeadLetterQueue() {
        return deadLetterQueue;
    }

    /**
     * 获取指定 Topic 的待重试消息数量。
     */
    public int getRetryMessageCount(String topic) {
        ConcurrentHashMap<String, RetryMessage> msgMap = retryMessageStore.get(topic);
        return msgMap != null ? msgMap.size() : 0;
    }

    /**
     * 获取所有待重试消息总数。
     */
    public int getTotalRetryMessageCount() {
        int total = 0;
        for (ConcurrentHashMap<String, RetryMessage> msgMap : retryMessageStore.values()) {
            total += msgMap.size();
        }
        return total;
    }

    /**
     * 清空指定 Topic 的重试消息。
     */
    public void clearRetryMessages(String topic) {
        retryMessageStore.remove(topic);
        log.info("Retry messages cleared: group={}, topic={}", consumerGroup, topic);
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    /** 本实例实际生效的最大重投次数（生产默认 {@value #DEFAULT_MAX_RECONSUME_TIMES}）. */
    public int getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    /** 本实例实际生效的到期检查周期（生产默认 {@value #DEFAULT_RETRY_CHECK_INTERVAL_MILLIS} ms）. */
    public long getRetryCheckIntervalMillis() {
        return retryCheckIntervalMillis;
    }

    /** 本实例实际生效的重投间隔策略. */
    public RetryDelayStrategy getDelayStrategy() {
        return delayStrategy;
    }

    @Override
    public String toString() {
        return "ConsumeRetryService{group='" + consumerGroup + "', maxRetries=" + maxReconsumeTimes
                + ", policy=" + retryPolicy + ", totalRetrying=" + getTotalRetryMessageCount()
                + ", dlq=" + deadLetterQueue + "}";
    }
}
