package com.taotao.cloud.mq.plugin.store;

import com.taotao.cloud.mq.common.message.MessageExt;

/**
 * Broker 内部消息（对标 RocketMQ MessageExtBrokerInner）.
 * <p>
 * 在 CommitLog 写入时由 Broker 构造，包含存储需要的元信息（storeTimestamp, commitLogOffset, bodyCRC）。
 */
public class MessageExtBrokerInner extends MessageExt {

    private static final long serialVersionUID = 1L;

    private String propertiesString;
    private long storeTimestamp;
    private long commitLogOffset;
    private int bodyCRC;

    public MessageExtBrokerInner() {
        super();
    }

    public String getPropertiesString() {
        return propertiesString;
    }

    public void setPropertiesString(String propertiesString) {
        this.propertiesString = propertiesString;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
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
}
