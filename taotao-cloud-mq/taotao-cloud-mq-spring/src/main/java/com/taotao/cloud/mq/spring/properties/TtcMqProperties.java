package com.taotao.cloud.mq.spring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ttc-MQ 配置属性.
 * <p>
 * 使用示例 (application.yml):
 * <pre>
 * ttc:
 *   namesrv-addr: localhost:9876
 *   producer:
 *     group: my-producer-group
 *     send-timeout-millis: 3000
 *     default-queue-nums: 4
 *   consumer:
 *     group: my-consumer-group
 *     pull-interval-millis: 1000
 *     pull-batch-size: 32
 * </pre>
 */
@ConfigurationProperties(prefix = "ttc.mq")
public class TtcMqProperties {

    /** NameServer 地址 (多个用分号分隔, 如 "host1:9876;host2:9876"). */
    private String namesrvAddr = "localhost:9876";

    /** 是否启用 TtcMq 客户端 (可动态关闭). */
    private boolean enabled = true;

    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Producer 配置.
     */
    public static class Producer {
        /** 生产者组名. */
        private String group = "ttc-default-producer-group";
        /** 发送超时 (ms). */
        private long sendTimeoutMillis = 3000;
        /** 默认 Topic 队列数. */
        private int defaultTopicQueueNums = 4;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public long getSendTimeoutMillis() {
            return sendTimeoutMillis;
        }

        public void setSendTimeoutMillis(long sendTimeoutMillis) {
            this.sendTimeoutMillis = sendTimeoutMillis;
        }

        public int getDefaultTopicQueueNums() {
            return defaultTopicQueueNums;
        }

        public void setDefaultTopicQueueNums(int defaultTopicQueueNums) {
            this.defaultTopicQueueNums = defaultTopicQueueNums;
        }
    }

    /**
     * Consumer 配置.
     */
    public static class Consumer {
        /** 消费者组名. */
        private String group = "ttc-default-consumer-group";
        /** 拉取间隔 (ms). */
        private long pullIntervalMillis = 1000;
        /** 每次拉取最大消息数. */
        private int pullBatchSize = 32;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public long getPullIntervalMillis() {
            return pullIntervalMillis;
        }

        public void setPullIntervalMillis(long pullIntervalMillis) {
            this.pullIntervalMillis = pullIntervalMillis;
        }

        public int getPullBatchSize() {
            return pullBatchSize;
        }

        public void setPullBatchSize(int pullBatchSize) {
            this.pullBatchSize = pullBatchSize;
        }
    }
}
