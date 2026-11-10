package com.taotao.cloud.mq.dashboard.web.dto;

/**
 * 消息查询入参，对应前端 {@code POST /messages/query} 的 body.
 * <p>
 * 前端 {@code queryMessages} 提交的是 {topic, queueId, offset, count}，这里沿用同一组键名，
 * 只把 {@code offset} 映射到协议层的 {@code queueOffset}。
 */
public class MessageQueryRequest {

    private String topic;
    private int queueId;
    private long offset;
    private int count = 32;
    /** 可选：直接指定 broker；留空则由服务端挑集群里第一个 broker。 */
    private String brokerAddr;

    public String getTopic() { return topic; }
    public void setTopic(String v) { this.topic = v; }
    public int getQueueId() { return queueId; }
    public void setQueueId(int v) { this.queueId = v; }
    public long getOffset() { return offset; }
    public void setOffset(long v) { this.offset = v; }
    public int getCount() { return count; }
    public void setCount(int v) { this.count = v; }
    public String getBrokerAddr() { return brokerAddr; }
    public void setBrokerAddr(String v) { this.brokerAddr = v; }
}
