package com.taotao.cloud.mq.plugin.store;

/**
 * 追加消息结果（对标 RocketMQ AppendMessageResult）.
 */
public class AppendMessageResult {

    public enum AppendMessageStatus {
        PUT_OK,
        END_OF_FILE,
        MESSAGE_SIZE_EXCEEDED,
        UNKNOWN_ERROR
    }

    private final AppendMessageStatus status;
    private final long wroteOffset;
    private final int wroteBytes;
    private final long storeTimestamp;

    public AppendMessageResult(AppendMessageStatus status, long wroteOffset, int wroteBytes, long storeTimestamp) {
        this.status = status;
        this.wroteOffset = wroteOffset;
        this.wroteBytes = wroteBytes;
        this.storeTimestamp = storeTimestamp;
    }

    public AppendMessageStatus getStatus() {
        return status;
    }

    public long getWroteOffset() {
        return wroteOffset;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public long getWroteBytesLong() {
        return wroteBytes;
    }
}
