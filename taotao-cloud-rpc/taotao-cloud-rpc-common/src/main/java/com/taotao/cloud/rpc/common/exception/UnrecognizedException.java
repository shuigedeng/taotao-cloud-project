package com.taotao.cloud.ttcrpc.common.exception;

/**
 * 无法识别错误
 */
public class UnrecognizedException extends RpcException {

	public UnrecognizedException() {
		super();
	}

	public UnrecognizedException(String message) {
		super(message);
	}
}
