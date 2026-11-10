package com.taotao.cloud.mq.common.message;

import java.net.InetSocketAddress;

/**
 * 扩展消息类
 * 在Message基础上增加了Broker存储相关的属性
 */
public class MessageExt extends Message {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID（全局唯一）
     */
    private String msgId;

    /**
     * 队列ID
     */
    private int queueId;

    /**
     * 存储大小
     */
    private int storeSize;

    /**
     * 队列中的偏移量
     */
    private long queueOffset;

    /**
     * 系统标志
     */
    private int sysFlag;

    /**
     * 消息产生时间戳
     */
    private long bornTimestamp;

    /**
     * 消息产生主机地址
     */
    private InetSocketAddress bornHost;

    /**
     * 消息存储时间戳
     */
    private long storeTimestamp;

    /**
     * 消息存储主机地址
     */
    private InetSocketAddress storeHost;

    /**
     * CommitLog中的物理偏移量
     */
    private long commitLogOffset;

    /**
     * 消息体CRC32校验值
     */
    private int bodyCRC;

    /**
     * 重试消费次数
     */
    private int reconsumeTimes;

    /**
     * 事务消息相关偏移量
     */
    private long preparedTransactionOffset;

    public MessageExt() {
        super();
    }

    /**
     * 从Message创建MessageExt
     */
    public static MessageExt fromMessage(Message message) {
        MessageExt ext = new MessageExt();
        ext.setTopic(message.getTopic());
        ext.setTags(message.getTags());
        ext.setKeys(message.getKeys());
        ext.setFlag(message.getFlag());
        ext.setProperties(message.getProperties());
        ext.setBody(message.getBody());
        ext.setTransactionId(message.getTransactionId());
        return ext;
    }

    /**
     * 转换为普通Message
     */
    public Message toMessage() {
        Message message = new Message();
        message.setTopic(this.getTopic());
        message.setTags(this.getTags());
        message.setKeys(this.getKeys());
        message.setFlag(this.getFlag());
        message.setProperties(this.getProperties());
        message.setBody(this.getBody());
        message.setTransactionId(this.getTransactionId());
        return message;
    }

    /**
     * 获取消息延迟级别
     */
    public int getDelayTimeLevel() {
        String t = this.getProperty("DELAY");
        if (t != null) {
            return Integer.parseInt(t);
        }
        return 0;
    }

    /**
     * 判断是否是重试消息
     */
    public boolean isRetry() {
        String retry = this.getProperty("RETRY");
        return retry != null && "true".equals(retry);
    }

    // ==================== Getters and Setters ====================

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getStoreSize() {
        return storeSize;
    }

    public void setStoreSize(int storeSize) {
        this.storeSize = storeSize;
    }

    public long getQueueOffset() {
        return queueOffset;
    }

    public void setQueueOffset(long queueOffset) {
        this.queueOffset = queueOffset;
    }

    public int getSysFlag() {
        return sysFlag;
    }

    public void setSysFlag(int sysFlag) {
        this.sysFlag = sysFlag;
    }

    public long getBornTimestamp() {
        return bornTimestamp;
    }

    public void setBornTimestamp(long bornTimestamp) {
        this.bornTimestamp = bornTimestamp;
    }

    public InetSocketAddress getBornHost() {
        return bornHost;
    }

    public void setBornHost(InetSocketAddress bornHost) {
        this.bornHost = bornHost;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }

    public InetSocketAddress getStoreHost() {
        return storeHost;
    }

    public void setStoreHost(InetSocketAddress storeHost) {
        this.storeHost = storeHost;
    }

    public long getCommitLogOffset() {
        return commitLogOffset;
    }

    public void setCommitLogOffset(long commitLogOffset) {
        this.commitLogOffset = commitLogOffset;
    }

    public int getBodyCRC() {
        return bodyCRC;
    }

    public void setBodyCRC(int bodyCRC) {
        this.bodyCRC = bodyCRC;
    }

    public int getReconsumeTimes() {
        return reconsumeTimes;
    }

    public void setReconsumeTimes(int reconsumeTimes) {
        this.reconsumeTimes = reconsumeTimes;
    }

    public long getPreparedTransactionOffset() {
        return preparedTransactionOffset;
    }

    public void setPreparedTransactionOffset(long preparedTransactionOffset) {
        this.preparedTransactionOffset = preparedTransactionOffset;
    }

    @Override
    public String toString() {
        return "MessageExt{" +
                "msgId='" + msgId + '\'' +
                ", queueId=" + queueId +
                ", storeSize=" + storeSize +
                ", queueOffset=" + queueOffset +
                ", sysFlag=" + sysFlag +
                ", bornTimestamp=" + bornTimestamp +
                ", bornHost=" + bornHost +
                ", storeTimestamp=" + storeTimestamp +
                ", storeHost=" + storeHost +
                ", commitLogOffset=" + commitLogOffset +
                ", bodyCRC=" + bodyCRC +
                ", reconsumeTimes=" + reconsumeTimes +
                ", preparedTransactionOffset=" + preparedTransactionOffset +
                ", " + super.toString() +
                '}';
    }
}
