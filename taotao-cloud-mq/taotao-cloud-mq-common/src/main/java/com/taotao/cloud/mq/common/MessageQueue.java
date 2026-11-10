package com.taotao.cloud.mq.common;

import java.io.Serializable;

/**
 * 消息队列（对标 RocketMQ MessageQueue）.
 * <p>
 * 轻量描述 (topic, brokerName, queueId) 三元组，用于 Producer/Consumer 路由。
 */
public class MessageQueue implements Serializable {

    private static final long serialVersionUID = 1L;

    private String topic;
    private String brokerName;
    private int queueId;

    public MessageQueue() {
    }

    public MessageQueue(String topic, String brokerName, int queueId) {
        this.topic = topic;
        this.brokerName = brokerName;
        this.queueId = queueId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }

        if (!(o instanceof MessageQueue)) { return false; }
        MessageQueue that = (MessageQueue) o;
        return queueId == that.queueId
                && (topic != null ? topic.equals(that.topic) : that.topic == null)
                && (brokerName != null ? brokerName.equals(that.brokerName) : that.brokerName == null);
    }

    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + (brokerName != null ? brokerName.hashCode() : 0);
        result = 31 * result + queueId;
        return result;
    }

    @Override
    public String toString() {
        return "MessageQueue{" +
                "topic='" + topic + '\'' +
                ", brokerName='" + brokerName + '\'' +
                ", queueId=" + queueId +
                '}';
    }
}
