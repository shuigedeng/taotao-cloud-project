package com.taotao.cloud.rpc.common.exception;

/**
 * 异步时间设置不合理异常
 */
public class AsyncTimeUnreasonableException extends RpcException {

	public AsyncTimeUnreasonableException() {
		super();
	}

	public AsyncTimeUnreasonableException(String message) {
		super(message);
	}
}
