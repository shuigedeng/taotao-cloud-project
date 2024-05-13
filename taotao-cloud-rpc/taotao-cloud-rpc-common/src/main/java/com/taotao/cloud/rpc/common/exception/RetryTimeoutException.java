package com.taotao.cloud.rpc.common.exception;

/**
 * 重试超时异常
 */
public class RetryTimeoutException extends RpcException {

	public RetryTimeoutException() {
		super();
	}

	public RetryTimeoutException(String message) {
		super(message);
	}
}
