package com.taotao.cloud.job.client.producer.entity;

import com.taotao.cloud.job.client.producer.InvokeCallback;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResponseFuture {
    private final long beginTimestamp = System.currentTimeMillis();
    @Setter
    private boolean sendResponseOK;

    private final InvokeCallback invokeCallback;

    private final long timeoutMillis = 5000;

    public ResponseFuture(InvokeCallback invokeCallback) {
        this.invokeCallback = invokeCallback;
    }

    public boolean isTimeout() {
        long diff = System.currentTimeMillis() - this.beginTimestamp;
        return diff > this.timeoutMillis;
    }
    public void executeInvokeCallback(){
        invokeCallback.operationComplete(this);
    }
}
