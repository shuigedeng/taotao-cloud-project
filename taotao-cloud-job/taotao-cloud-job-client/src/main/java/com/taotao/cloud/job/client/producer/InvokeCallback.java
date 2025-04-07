package com.taotao.cloud.job.client.producer;


import com.taotao.cloud.job.client.producer.entity.ResponseFuture;

public interface InvokeCallback {
	void operationComplete(final ResponseFuture responseFuture);
}
