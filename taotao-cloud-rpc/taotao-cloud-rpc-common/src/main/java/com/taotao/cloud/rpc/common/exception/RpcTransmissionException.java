package com.taotao.cloud.rpc.common.exception;

/**
 * 数据传输异常
 */
public class RpcTransmissionException extends RpcException {

	public RpcTransmissionException() {
		super();
	}

	public RpcTransmissionException(String message) {
		super(message);
	}
}
