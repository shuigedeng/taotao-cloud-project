package com.taotao.cloud.mq.common;

import com.taotao.cloud.mq.common.filter.FilterType;

import java.io.Serializable;

/**
 * 消费者组订阅配置（对标 RocketMQ SubscriptionGroupConfig）.
 * <p>
 * 存储消费者组的订阅信息，包括 Topic、过滤类型和过滤表达式。
 * Broker 端根据此配置为消费者提供消息过滤服务。
 */
public class SubscriptionGroupConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 消费者组名 */
    private String consumerGroup;

    /** 订阅的 Topic */
    private String topic;

    /** 过滤类型（TAG / SQL92） */
    private FilterType filterType;

    /** 过滤表达式 */
    private String filterExpression;

    /** 是否广播消费模式 */
    private boolean broadcasting;

    /** 消费模式：并发消费 / 顺序消费 */
    private ConsumeMode consumeMode = ConsumeMode.CONCURRENTLY;

    /**
     * 消费模式枚举。
     */
    public enum ConsumeMode {
        /** 并发消费（默认） */
        CONCURRENTLY,
        /** 顺序消费 */
        ORDERLY
    }

    public SubscriptionGroupConfig() {
    }

    public SubscriptionGroupConfig(String consumerGroup, String topic) {
        this.consumerGroup = consumerGroup;
        this.topic = topic;
        this.filterType = FilterType.TAG;
        this.filterExpression = "*";
    }

    public SubscriptionGroupConfig(String consumerGroup, String topic, FilterType filterType, String filterExpression) {
        this.consumerGroup = consumerGroup;
        this.topic = topic;
        this.filterType = filterType;
        this.filterExpression = filterExpression;
    }

    // ==================== Getters and Setters ====================

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }

    public void setBroadcasting(boolean broadcasting) {
        this.broadcasting = broadcasting;
    }

    public ConsumeMode getConsumeMode() {
        return consumeMode;
    }

    public void setConsumeMode(ConsumeMode consumeMode) {
        this.consumeMode = consumeMode;
    }

    @Override
    public String toString() {
        return "SubscriptionGroupConfig{" +
                "consumerGroup='" + consumerGroup + '\'' +
                ", topic='" + topic + '\'' +
                ", filterType=" + filterType +
                ", filterExpression='" + filterExpression + '\'' +
                ", broadcasting=" + broadcasting +
                ", consumeMode=" + consumeMode +
                '}';
    }
}
