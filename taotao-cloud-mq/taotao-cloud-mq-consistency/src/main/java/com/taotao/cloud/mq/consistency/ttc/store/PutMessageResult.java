package com.taotao.cloud.mq.consistency.ttc.store;

import com.taotao.cloud.mq.consistency.ttc.store.AppendMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageStatus;

/**
 * 写入消息结果（对标 RocketMQ PutMessageResult）.
 */
public class PutMessageResult {

    private final PutMessageStatus putMessageStatus;
    private final AppendMessageResult appendMessageResult;

    public PutMessageResult(PutMessageStatus putMessageStatus, AppendMessageResult appendMessageResult) {
        this.putMessageStatus = putMessageStatus;
        this.appendMessageResult = appendMessageResult;
    }

    public PutMessageStatus getPutMessageStatus() {
        return putMessageStatus;
    }

    public void setPutMessageStatus(PutMessageStatus putMessageStatus) {
        // deprecated setter retained for backward compatibility
    }

    public AppendMessageResult getAppendMessageResult() {
        return appendMessageResult;
    }

    public boolean isOk() {
        return putMessageStatus == PutMessageStatus.PUT_OK;
    }
}
