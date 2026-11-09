package com.taotao.cloud.mq.consistency.ttc.common.trace;

import com.taotao.cloud.mq.consistency.ttc.common.trace.TraceType;

import java.io.Serializable;

/**
 * 消息轨迹数据（对标 RocketMQ TraceBean）.
 * <p>
 * 记录消息在各个阶段的轨迹信息：
 * <ul>
 *   <li>Producer 发送</li>
 *   <li>Broker 存储</li>
 *   <li>Consumer 消费</li>
 * </ul>
 */
public class TraceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 轨迹类型 */
    private TraceType traceType;

    /** Topic */
    private String topic;

    /** 消息 ID */
    private String msgId;

    /** 消息 Key */
    private String keys;

    /** Tag */
    private String tags;

    /** Broker 地址 */
    private String brokerAddr;

    /** 生产者地址 */
    private String producerAddr;

    /** 消费者地址 */
    private String consumerAddr;

    /** 消费者组 */
    private String consumerGroup;

    /** 生产者组 */
    private String producerGroup;

    /** 发送状态 */
    private String sendStatus;

    /** 消费状态 */
    private String consumeStatus;

    /** 发送时间戳 */
    private long sendTimestamp;

    /** 存储时间戳 */
    private long storeTimestamp;

    /** 消费时间戳 */
    private long consumeTimestamp;

    /** 发送耗时（毫秒） */
    private long costTime;

    /** 错误信息 */
    private String errorMsg;

    /** 轨迹上下文（用于链路追踪） */
    private String traceContext;

    public TraceBean() {
        this.sendTimestamp = System.currentTimeMillis();
    }

    public TraceBean(TraceType traceType, String topic, String msgId) {
        this();
        this.traceType = traceType;
        this.topic = topic;
        this.msgId = msgId;
    }

    // ==================== Getters and Setters ====================

    public TraceType getTraceType() {
        return traceType;
    }

    public void setTraceType(TraceType traceType) {
        this.traceType = traceType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBrokerAddr() {
        return brokerAddr;
    }

    public void setBrokerAddr(String brokerAddr) {
        this.brokerAddr = brokerAddr;
    }

    public String getProducerAddr() {
        return producerAddr;
    }

    public void setProducerAddr(String producerAddr) {
        this.producerAddr = producerAddr;
    }

    public String getConsumerAddr() {
        return consumerAddr;
    }

    public void setConsumerAddr(String consumerAddr) {
        this.consumerAddr = consumerAddr;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getConsumeStatus() {
        return consumeStatus;
    }

    public void setConsumeStatus(String consumeStatus) {
        this.consumeStatus = consumeStatus;
    }

    public long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }

    public long getConsumeTimestamp() {
        return consumeTimestamp;
    }

    public void setConsumeTimestamp(long consumeTimestamp) {
        this.consumeTimestamp = consumeTimestamp;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTraceContext() {
        return traceContext;
    }

    public void setTraceContext(String traceContext) {
        this.traceContext = traceContext;
    }

    @Override
    public String toString() {
        return "TraceBean{" +
                "traceType=" + traceType +
                ", topic='" + topic + '\'' +
                ", msgId='" + msgId + '\'' +
                ", keys='" + keys + '\'' +
                ", brokerAddr='" + brokerAddr + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", consumeStatus='" + consumeStatus + '\'' +
                ", sendTimestamp=" + sendTimestamp +
                ", storeTimestamp=" + storeTimestamp +
                ", consumeTimestamp=" + consumeTimestamp +
                ", costTime=" + costTime +
                '}';
    }
}
