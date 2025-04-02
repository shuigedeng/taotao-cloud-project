package com.taotao.cloud.job.client.producer;

import org.kjob.producer.entity.ResponseFuture;

public interface InvokeCallback {
    void operationComplete(final ResponseFuture responseFuture);
}
