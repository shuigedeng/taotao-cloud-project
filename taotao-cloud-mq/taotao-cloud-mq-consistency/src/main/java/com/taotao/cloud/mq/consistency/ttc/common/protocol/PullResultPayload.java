package com.taotao.cloud.mq.consistency.ttc.common.protocol;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;

import java.io.Serializable;
import java.util.List;

/**
 * 拉取结果 Body（对标 RocketMQ PullResult）.
 * <p>
 * 协议层共享 — Broker 写入此对象作为响应 Body, Client 解码为本地对象。
 */
public class PullResultPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String topic;
    private int queueId;
    private long nextOffset;
    private long minOffset;
    private long maxOffset;
    private List<MessageExt> messages;

    public PullResultPayload() {
    }

    public PullResultPayload(String topic, int queueId, long nextOffset,
                             long minOffset, long maxOffset, List<MessageExt> messages) {
        this.topic = topic;
        this.queueId = queueId;
        this.nextOffset = nextOffset;
        this.minOffset = minOffset;
        this.maxOffset = maxOffset;
        this.messages = messages;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public long getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(long nextOffset) {
        this.nextOffset = nextOffset;
    }

    public long getMinOffset() {
        return minOffset;
    }

    public void setMinOffset(long minOffset) {
        this.minOffset = minOffset;
    }

    public long getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(long maxOffset) {
        this.maxOffset = maxOffset;
    }

    public List<MessageExt> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageExt> messages) {
        this.messages = messages;
    }
}
