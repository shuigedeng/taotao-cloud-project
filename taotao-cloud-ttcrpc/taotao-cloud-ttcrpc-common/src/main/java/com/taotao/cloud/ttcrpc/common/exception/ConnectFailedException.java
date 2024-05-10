package com.taotao.cloud.ttcrpc.common.exception;

public class ConnectFailedException extends RpcException {

	public ConnectFailedException() {
		super();
	}

	public ConnectFailedException(String message) {
		super(message);
	}
}
