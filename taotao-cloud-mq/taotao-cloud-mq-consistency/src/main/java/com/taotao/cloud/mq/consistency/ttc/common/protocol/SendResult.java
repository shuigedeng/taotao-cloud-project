package com.taotao.cloud.mq.consistency.ttc.common.protocol;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;

import java.io.Serializable;

/**
 * 发送结果（对标 RocketMQ SendResult）.
 * <p>
 * 协议层共享 — Broker 写入此对象作为响应 Body, Client 解码后转换为面向用户的结果。
 */
public class SendResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msgId;
    private String topic;
    private int queueId;
    private long queueOffset;
    private SendStatus sendStatus;
    private String errorMsg;
    private MessageQueue messageQueue;

    public SendResult() {
        this.sendStatus = SendStatus.SEND_OK;
    }

    public SendResult(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public static SendResult ok(String msgId, String topic, int queueId, long queueOffset) {
        SendResult r = new SendResult(SendStatus.SEND_OK);
        r.setMsgId(msgId);
        r.setTopic(topic);
        r.setQueueId(queueId);
        r.setQueueOffset(queueOffset);
        return r;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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

    public long getQueueOffset() {
        return queueOffset;
    }

    public void setQueueOffset(long queueOffset) {
        this.queueOffset = queueOffset;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public String toString() {
        return "SendResult{" +
                "msgId='" + msgId + '\'' +
                ", topic='" + topic + '\'' +
                ", queueId=" + queueId +
                ", queueOffset=" + queueOffset +
                ", sendStatus=" + sendStatus +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
