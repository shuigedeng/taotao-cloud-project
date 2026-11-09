package com.taotao.cloud.mq.consistency.ttc.broker.metrics;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Broker 监控指标（对标 RocketMQ BrokerMetrics）.
 * <p>
 * 收集 Broker 的关键运行指标：
 * <ul>
 *   <li>消息发送 TPS</li>
 *   <li>消息消费 TPS</li>
 *   <li>消息堆积量</li>
 *   <li>Broker 存储大小</li>
 *   <li>Topic 级别指标</li>
 * </ul>
 * <p>
 * 支持 Prometheus 格式导出：
 * <pre>
 * # HELP z_mq_broker_send_tps Broker send TPS
 * # TYPE z_mq_broker_send_tps gauge
 * z_mq_broker_send_tps 1234
 * </pre>
 */
public class BrokerMetrics {

    private static final Logger log = LoggerFactory.getLogger(BrokerMetrics.class);

    // ==================== 全局指标 ====================

    /** 消息发送总数 */
    private final AtomicLong totalSendMessages = new AtomicLong(0);

    /** 消息消费总数 */
    private final AtomicLong totalConsumeMessages = new AtomicLong(0);

    /** 消息发送失败数 */
    private final AtomicLong failedSendMessages = new AtomicLong(0);

    /** 消息消费失败数 */
    private final AtomicLong failedConsumeMessages = new AtomicLong(0);

    /** 消息堆积量（待消费） */
    private final AtomicLong messageBacklog = new AtomicLong(0);

    /** Broker 存储大小（字节） */
    private final AtomicLong storeSize = new AtomicLong(0);

    /** CommitLog 文件数 */
    private final AtomicLong commitLogCount = new AtomicLong(0);

    /** Topic 数量 */
    private final AtomicLong topicCount = new AtomicLong(0);

    /** 消费者组数量 */
    private final AtomicLong consumerGroupCount = new AtomicLong(0);

    /** 活跃连接数 */
    private final AtomicLong activeConnections = new AtomicLong(0);

    // ==================== TPS 计算 ====================

    /** 上次采样时间戳 */
    private volatile long lastSampleTimestamp = System.currentTimeMillis();

    /** 上次采样时的发送总数 */
    private volatile long lastSendCount = 0;

    /** 上次采样时的消费总数 */
    private volatile long lastConsumeCount = 0;

    /** 当前发送 TPS */
    private volatile double sendTps = 0;

    /** 当前消费 TPS */
    private volatile double consumeTps = 0;

    // ==================== Topic 级别指标 ====================

    /** Topic 级别发送计数: topic -> count */
    private final ConcurrentHashMap<String, AtomicLong> topicSendCount = new ConcurrentHashMap<>();

    /** Topic 级别消费计数: topic -> count */
    private final ConcurrentHashMap<String, AtomicLong> topicConsumeCount = new ConcurrentHashMap<>();

    /** Topic 级别堆积量: topic -> backlog */
    private final ConcurrentHashMap<String, AtomicLong> topicBacklog = new ConcurrentHashMap<>();

    private static final BrokerMetrics INSTANCE = new BrokerMetrics();

    public static BrokerMetrics getInstance() {
        return INSTANCE;
    }

    private BrokerMetrics() {
    }

    // ==================== 指标更新方法 ====================

    /**
     * 记录消息发送。
     */
    public void recordSendMessage(String topic) {
        totalSendMessages.incrementAndGet();
        topicSendCount.computeIfAbsent(topic, k -> new AtomicLong()).incrementAndGet();
    }

    /**
     * 记录消息消费。
     */
    public void recordConsumeMessage(String topic) {
        totalConsumeMessages.incrementAndGet();
        topicConsumeCount.computeIfAbsent(topic, k -> new AtomicLong()).incrementAndGet();
    }

    /**
     * 记录发送失败。
     */
    public void recordSendFailed() {
        failedSendMessages.incrementAndGet();
    }

    /**
     * 记录消费失败。
     */
    public void recordConsumeFailed() {
        failedConsumeMessages.incrementAndGet();
    }

    /**
     * 更新消息堆积量。
     */
    public void updateBacklog(long backlog) {
        messageBacklog.set(backlog);
    }

    /**
     * 更新 Topic 级别堆积量。
     */
    public void updateTopicBacklog(String topic, long backlog) {
        topicBacklog.computeIfAbsent(topic, k -> new AtomicLong()).set(backlog);
    }

    /**
     * 更新存储大小。
     */
    public void updateStoreSize(long size) {
        storeSize.set(size);
    }

    /**
     * 更新 Topic 数量。
     */
    public void updateTopicCount(long count) {
        topicCount.set(count);
    }

    /**
     * 更新消费者组数量。
     */
    public void updateConsumerGroupCount(long count) {
        consumerGroupCount.set(count);
    }

    /**
     * 更新活跃连接数。
     */
    public void updateActiveConnections(long count) {
        activeConnections.set(count);
    }

    /**
     * 计算 TPS（每秒调用一次）。
     */
    public void calculateTps() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastSampleTimestamp;
        if (elapsed > 0) {
            long currentSendCount = totalSendMessages.get();
            long currentConsumeCount = totalConsumeMessages.get();
            sendTps = (double) (currentSendCount - lastSendCount) * 1000 / elapsed;
            consumeTps = (double) (currentConsumeCount - lastConsumeCount) * 1000 / elapsed;
            lastSendCount = currentSendCount;
            lastConsumeCount = currentConsumeCount;
            lastSampleTimestamp = now;
        }
    }

    // ==================== 指标获取方法 ====================

    public long getTotalSendMessages() {
        return totalSendMessages.get();
    }

    public long getTotalConsumeMessages() {
        return totalConsumeMessages.get();
    }

    public long getFailedSendMessages() {
        return failedSendMessages.get();
    }

    public long getFailedConsumeMessages() {
        return failedConsumeMessages.get();
    }

    public long getMessageBacklog() {
        return messageBacklog.get();
    }

    public long getStoreSize() {
        return storeSize.get();
    }

    public double getSendTps() {
        return sendTps;
    }

    public double getConsumeTps() {
        return consumeTps;
    }

    public long getTopicCount() {
        return topicCount.get();
    }

    public long getConsumerGroupCount() {
        return consumerGroupCount.get();
    }

    public long getActiveConnections() {
        return activeConnections.get();
    }

    public long getTopicSendCount(String topic) {
        AtomicLong count = topicSendCount.get(topic);
        return count != null ? count.get() : 0;
    }

    public long getTopicConsumeCount(String topic) {
        AtomicLong count = topicConsumeCount.get(topic);
        return count != null ? count.get() : 0;
    }

    public long getTopicBacklog(String topic) {
        AtomicLong backlog = topicBacklog.get(topic);
        return backlog != null ? backlog.get() : 0;
    }

    // ==================== Prometheus 格式导出 ====================

    /**
     * 导出为 Prometheus 文本格式。
     */
    public String toPrometheusFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append("# HELP z_mq_broker_send_total Total messages sent\n");
        sb.append("# TYPE z_mq_broker_send_total counter\n");
        sb.append("z_mq_broker_send_total ").append(totalSendMessages.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_consume_total Total messages consumed\n");
        sb.append("# TYPE z_mq_broker_consume_total counter\n");
        sb.append("z_mq_broker_consume_total ").append(totalConsumeMessages.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_send_failed_total Total send failures\n");
        sb.append("# TYPE z_mq_broker_send_failed_total counter\n");
        sb.append("z_mq_broker_send_failed_total ").append(failedSendMessages.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_consume_failed_total Total consume failures\n");
        sb.append("# TYPE z_mq_broker_consume_failed_total counter\n");
        sb.append("z_mq_broker_consume_failed_total ").append(failedConsumeMessages.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_send_tps Current send TPS\n");
        sb.append("# TYPE z_mq_broker_send_tps gauge\n");
        sb.append("z_mq_broker_send_tps ").append(String.format("%.2f", sendTps)).append("\n\n");

        sb.append("# HELP z_mq_broker_consume_tps Current consume TPS\n");
        sb.append("# TYPE z_mq_broker_consume_tps gauge\n");
        sb.append("z_mq_broker_consume_tps ").append(String.format("%.2f", consumeTps)).append("\n\n");

        sb.append("# HELP z_mq_broker_message_backlog Message backlog count\n");
        sb.append("# TYPE z_mq_broker_message_backlog gauge\n");
        sb.append("z_mq_broker_message_backlog ").append(messageBacklog.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_store_size Store size in bytes\n");
        sb.append("# TYPE z_mq_broker_store_size gauge\n");
        sb.append("z_mq_broker_store_size ").append(storeSize.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_topic_count Topic count\n");
        sb.append("# TYPE z_mq_broker_topic_count gauge\n");
        sb.append("z_mq_broker_topic_count ").append(topicCount.get()).append("\n\n");

        sb.append("# HELP z_mq_broker_active_connections Active connections\n");
        sb.append("# TYPE z_mq_broker_active_connections gauge\n");
        sb.append("z_mq_broker_active_connections ").append(activeConnections.get()).append("\n\n");

        // Topic 级别指标
        for (Map.Entry<String, AtomicLong> entry : topicSendCount.entrySet()) {
            sb.append("z_mq_broker_topic_send_total{topic=\"").append(entry.getKey())
                    .append("\"} ").append(entry.getValue().get()).append("\n");
        }

        return sb.toString();
    }

    /**
     * 导出为 JSON 格式。
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sendTotal\":").append(totalSendMessages.get()).append(",");
        sb.append("\"consumeTotal\":").append(totalConsumeMessages.get()).append(",");
        sb.append("\"sendFailed\":").append(failedSendMessages.get()).append(",");
        sb.append("\"consumeFailed\":").append(failedConsumeMessages.get()).append(",");
        sb.append("\"sendTps\":").append(String.format("%.2f", sendTps)).append(",");
        sb.append("\"consumeTps\":").append(String.format("%.2f", consumeTps)).append(",");
        sb.append("\"messageBacklog\":").append(messageBacklog.get()).append(",");
        sb.append("\"storeSize\":").append(storeSize.get()).append(",");
        sb.append("\"topicCount\":").append(topicCount.get()).append(",");
        sb.append("\"activeConnections\":").append(activeConnections.get());
        sb.append("}");
        return sb.toString();
    }

    /**
     * 重置所有指标。
     */
    public void reset() {
        totalSendMessages.set(0);
        totalConsumeMessages.set(0);
        failedSendMessages.set(0);
        failedConsumeMessages.set(0);
        messageBacklog.set(0);
        storeSize.set(0);
        topicSendCount.clear();
        topicConsumeCount.clear();
        topicBacklog.clear();
        log.info("BrokerMetrics reset");
    }
}
