package com.taotao.cloud.mq.common.enums;

import com.taotao.cloud.mq.common.message.Message;

/**
 * 一条"等待回查的事务"的快照，broker 在回查请求的响应体里把它回给生产者。
 * <p>
 * 放在 {@code client.producer} 而不是 broker 侧，是因为这条记录的两侧都要能读写：broker 已经依赖
 * client（{@code TransactionMessageProcessor} 就 import 了 {@link TransactionState}），
 * 反过来让 client 依赖 broker 会把这个模块的依赖方向扭断。
 */
public class TransactionCheckTask {

    private String transactionId;
    /** 半消息背后那条业务消息真正的 topic。 */
    private String originalTopic;
    /** 提交后消息要回投到哪个队列。 */
    private int originalQueueId;
    private String msgId;
    private Message halfMessage;
    private int checkTimes;
    private long createTimestamp;

    public TransactionCheckTask() {
    }

    public TransactionCheckTask(String transactionId, String originalTopic, int originalQueueId,
                                String msgId, Message halfMessage, int checkTimes, long createTimestamp) {
        this.transactionId = transactionId;
        this.originalTopic = originalTopic;
        this.originalQueueId = originalQueueId;
        this.msgId = msgId;
        this.halfMessage = halfMessage;
        this.checkTimes = checkTimes;
        this.createTimestamp = createTimestamp;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getOriginalTopic() { return originalTopic; }
    public void setOriginalTopic(String originalTopic) { this.originalTopic = originalTopic; }
    public int getOriginalQueueId() { return originalQueueId; }
    public void setOriginalQueueId(int originalQueueId) { this.originalQueueId = originalQueueId; }
    public String getMsgId() { return msgId; }
    public void setMsgId(String msgId) { this.msgId = msgId; }
    public Message getHalfMessage() { return halfMessage; }
    public void setHalfMessage(Message halfMessage) { this.halfMessage = halfMessage; }
    public int getCheckTimes() { return checkTimes; }
    public void setCheckTimes(int checkTimes) { this.checkTimes = checkTimes; }
    public long getCreateTimestamp() { return createTimestamp; }
    public void setCreateTimestamp(long createTimestamp) { this.createTimestamp = createTimestamp; }
}
