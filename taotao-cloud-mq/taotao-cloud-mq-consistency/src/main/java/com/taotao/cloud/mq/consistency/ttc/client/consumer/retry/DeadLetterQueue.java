package com.taotao.cloud.mq.consistency.ttc.client.consumer.retry;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 死信队列管理器（对标 RocketMQ DeadLetterQueue）.
 * <p>
 * 当消息消费重试次数超过最大限制时，自动转入死信队列。
 * 死信队列 Topic 命名规则：{@code %DLQ%{consumerGroup}}
 * <p>
 * 消费者可以订阅死信 Topic 进行人工处理或告警。
 */
public class DeadLetterQueue {

    private static final Logger log = LoggerFactory.getLogger(DeadLetterQueue.class);

    /** 死信队列 Topic 前缀 */
    public static final String DLQ_TOPIC_PREFIX = "%DLQ%";

    /** 默认死信消息保留时间（3天） */
    public static final long DEFAULT_RETENTION_MILLIS = 3 * 24 * 60 * 60 * 1000L;

    /** 进程内缓存最多留多少条：真相在死信 Topic 里，这里只兜"发不出去"的那部分 */
    public static final int MAX_CACHED_DEAD_LETTERS = 1024;

    /** 死信队列存储 */
    private final ConcurrentLinkedQueue<DeadLetterMessage> deadLetterQueue = new ConcurrentLinkedQueue<>();

    /** 统计计数器 */
    private final AtomicLong totalDeadLetters = new AtomicLong(0);
    private final AtomicLong totalExpired = new AtomicLong(0);

    /**
     * 「这一条已经转入过死信」的记账凭据：同一条消息（同一 Topic、同一 msgId、同一重投次数）
     * 在发不出去的时候会被一遍遍重拉，每被重拉一次就是一次完整的转入 —— 而记账只该有第一笔。
     * <p>
     * 这份凭据独立于缓存本体：{@link #pollAll()} 把缓存取走之后，这条消息仍然已经被记过账了，
     * 否则「取走一次」就把幂等性洗掉，下一次重拉又会多出一份同样的副本。
     * <p>
     * 容量与缓存同档（{@link #MAX_CACHED_DEAD_LETTERS} 条），按访问顺序淘汰最久没被提到的键，
     * 因此它不会随运行时间无界增长；代价是超出容量的老键会被遗忘，那时同一条消息会被再记一笔账
     * —— 宁可重复记账，也不许因为记错了而把一条真没兜着的消息吞掉。
     */
    private final Map<String, Boolean> deadLetteredKeys = new LinkedHashMap<String, Boolean>(
            MAX_CACHED_DEAD_LETTERS * 4 / 3 + 1, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
            return size() > MAX_CACHED_DEAD_LETTERS;
        }
    };

    private final String consumerGroup;
    private final long retentionMillis;

    /**
     * 死信消息包装类。
     */
    public static class DeadLetterMessage {
        private final MessageExt message;
        private final int reconsumeTimes;
        private final long deadLetterTimestamp;
        private final String reason;
        private boolean published;

        public DeadLetterMessage(MessageExt message, int reconsumeTimes, String reason) {
            this.message = message;
            this.reconsumeTimes = reconsumeTimes;
            this.deadLetterTimestamp = System.currentTimeMillis();
            this.reason = reason;
        }

        public MessageExt getMessage() {
            return message;
        }

        public int getReconsumeTimes() {
            return reconsumeTimes;
        }

        public long getDeadLetterTimestamp() {
            return deadLetterTimestamp;
        }

        public String getReason() {
            return reason;
        }

        /** 这一条是否已经真发到死信 Topic 上（false = 只活在进程内这一份缓存里）. */
        public boolean isPublished() {
            return published;
        }

        public void setPublished(boolean published) {
            this.published = published;
        }
    }

    public DeadLetterQueue(String consumerGroup) {
        this(consumerGroup, DEFAULT_RETENTION_MILLIS);
    }

    public DeadLetterQueue(String consumerGroup, long retentionMillis) {
        this.consumerGroup = consumerGroup;
        this.retentionMillis = retentionMillis;
    }

    /**
     * 死信的发布通路：把这条消息真发到 {@link #getDlqTopic()} 那个 Topic 上。
     * <p>
     * 只有进程内那份队列的消息是掉电就没的；订阅"死信 Topic 做人工处理或告警"这句话
     * 要靠这条通路把消息送进存储才成立。
     */
    public interface DlqPublisher {
        /**
         * @param message        要转入死信的消息
         * @param reconsumeTimes 已经重投过的次数
         * @param dlqTopic       目标死信 Topic（由 {@link #getDlqTopic()} 算出）
         * @param reason         转入死信的原因
         * @return 是否已经被 broker 收下
         */
        boolean publish(MessageExt message, int reconsumeTimes, String dlqTopic, String reason);
    }

    /** 死信发布通路；没设置时只记进程内那一份 */
    private volatile DlqPublisher publisher;

    /**
     * 将消息转入死信队列。
     * <p>
     * 先往死信 Topic 发（那条才是掉电不丢的真相），再把这一条记进进程内队列当缓存与统计；
     * 发不出去时进程内这一份仍然留着，至少不丢读数。
     * <p>
     * 外投这一路<b>永远不判重</b>：只要被叫到就真发一次。位点提交之后正常不会再被重拉，
     * 真被重拉时宁可死信 Topic 上多一条，也不许"以为发过了"就不发。
     * 判重只发生在发不出去的那一路 —— 那条消息会被反复重拉，每次重拉都进这个方法，
     * 如果照单记账，缓存里就会堆同一份副本的 N 个分身，并且第 {@value #MAX_CACHED_DEAD_LETTERS} + 1
     * 个分身会把最早那一份"必须兜着"的条目挤出去。
     *
     * @param message        消息
     * @param reconsumeTimes 已重试次数
     * @param reason         转入死信的原因
     * @return 这条死信是否真的被交到了死信 Topic 上；false 表示只有进程内这一份缓存。
     *         调用方要靠这个读数决定"原来那条还要不要再兜着"。
     */
    public boolean putMessage(MessageExt message, int reconsumeTimes, String reason) {
        String dlqTopic = getDlqTopic();
        DlqPublisher current = this.publisher;
        boolean published = false;
        if (current != null) {
            try {
                published = current.publish(message, reconsumeTimes, dlqTopic, reason);
            } catch (Exception e) {
                log.warn("Dead letter publish failed: group={} topic={} err={}",
                        consumerGroup, dlqTopic, e.getMessage());
            }
        }
        if (published) {
            // 真相已经在死信 Topic 里，进程内再留一份就成了"看起来有两份真相"的第二持有者。
            totalDeadLetters.incrementAndGet();
            log.warn("Message moved to DLQ: topic={}, msgId={}, reconsumeTimes={}, reason={}, group={}"
                            + " dlqTopic={} published={}",
                    message.getTopic(), message.getMsgId(), reconsumeTimes, reason, consumerGroup,
                    dlqTopic, Boolean.TRUE);
            return true;
        }

        if (!firstDeadLetterOf(message, reconsumeTimes)) {
            // 同一条消息的再一次重拉：不 offer、不涨统计、不动过期计数，
            // 但"它没进死信 Topic"这个事实没变，所以照旧回 false。
            log.debug("Duplicate dead-letter delivery ignored: group={} topic={} msgId={} reconsumeTimes={}",
                    consumerGroup, message.getTopic(), message.getMsgId(), reconsumeTimes);
            return false;
        }

        DeadLetterMessage dlqMessage = new DeadLetterMessage(message, reconsumeTimes, reason);
        dlqMessage.setPublished(false);
        totalDeadLetters.incrementAndGet();
        deadLetterQueue.offer(dlqMessage);
        while (deadLetterQueue.size() > MAX_CACHED_DEAD_LETTERS && deadLetterQueue.poll() != null) {
            // 只裁缓存，计数器不动（统计口径是"进过死信多少条"，不是"缓存里还剩多少条"）
            totalExpired.incrementAndGet();
        }
        log.warn("Message moved to DLQ: topic={}, msgId={}, reconsumeTimes={}, reason={}, group={}"
                        + " dlqTopic={} published={}",
                message.getTopic(), message.getMsgId(), reconsumeTimes, reason, consumerGroup,
                dlqTopic, Boolean.FALSE);
        return false;
    }

    /**
     * 这条消息是不是第一次转入死信：第一次返回 true（该记账、该进缓存），
     * 之后同一条（同 Topic、同 msgId、同重投次数）再进来返回 false。
     * <p>
     * 认不出这条消息是谁的（Topic 或 msgId 为空）时一律当第一次 —— 判不了重就退回旧行为，
     * 宁可重复记一笔账，也不许把两条不同的消息当成一条、把后一条吞掉。
     */
    private boolean firstDeadLetterOf(MessageExt message, int reconsumeTimes) {
        String topic = message.getTopic();
        String msgId = message.getMsgId();
        if (topic == null || topic.isEmpty() || msgId == null || msgId.isEmpty()) {
            return true;
        }
        String key = dedupKeyOf(topic, msgId, reconsumeTimes);
        synchronized (deadLetteredKeys) {
            if (deadLetteredKeys.containsKey(key)) {
                return false;
            }
            deadLetteredKeys.put(key, Boolean.TRUE);
            return true;
        }
    }

    /**
     * 判重键：长度前缀自定界，所以 {@code (topic, msgId)} 的任何一种切法都不会和另一组撞成同一个串。
     */
    private static String dedupKeyOf(String topic, String msgId, int reconsumeTimes) {
        return topic.length() + ":" + topic + msgId.length() + ":" + msgId + '@' + reconsumeTimes;
    }

    /**
     * 设置死信发布通路（把死信真发到 {@link #getDlqTopic()}）.
     */
    public void setPublisher(DlqPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 本消费组的死信 Topic 名（命名规则见类注释）。
     */
    public String getDlqTopic() {
        return DLQ_TOPIC_PREFIX + consumerGroup;
    }

    /**
     * 从死信队列取出所有消息（用于消费者订阅处理）。
     * <p>
     * 取走只清缓存本体，不清"已经记过账"那份凭据：这条消息要是之后还被重拉，
     * 再往缓存里塞一份就是同一个死信的两份分身。
     *
     * @return 死信消息列表
     */
    public List<DeadLetterMessage> pollAll() {
        List<DeadLetterMessage> messages = new ArrayList<>();
        DeadLetterMessage msg;
        while ((msg = deadLetterQueue.poll()) != null) {
            // 检查是否过期
            if (System.currentTimeMillis() - msg.getDeadLetterTimestamp() <= retentionMillis) {
                messages.add(msg);
            } else {
                totalExpired.incrementAndGet();
            }
        }
        return messages;
    }

    /**
     * 获取死信队列大小。
     */
    public int size() {
        return deadLetterQueue.size();
    }

    /**
     * 获取总死信消息数。
     */
    public long getTotalDeadLetters() {
        return totalDeadLetters.get();
    }

    /**
     * 获取过期清理的消息数。
     */
    public long getTotalExpired() {
        return totalExpired.get();
    }

    /**
     * 清空死信队列：连同"已经记过账"那份凭据一起清零，等于把这个兜底容器整个重置。
     * <p>
     * 重置之后同一条消息再被重拉时会重新进缓存 —— 这是显式重置的语义，
     * 与 {@link #pollAll()}（取走内容但保留记账）不是一回事。
     */
    public void clear() {
        deadLetterQueue.clear();
        synchronized (deadLetteredKeys) {
            deadLetteredKeys.clear();
        }
        log.info("DLQ cleared: group={}", consumerGroup);
    }

    @Override
    public String toString() {
        return "DeadLetterQueue{group='" + consumerGroup + "', size=" + deadLetterQueue.size()
                + ", totalDeadLetters=" + totalDeadLetters.get()
                + ", totalExpired=" + totalExpired.get() + "}";
    }
}
