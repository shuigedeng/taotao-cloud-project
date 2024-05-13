package com.taotao.cloud.rpc.common.exception;

public class ConnectFailedException extends RpcException {

	public ConnectFailedException() {
		super();
	}

	public ConnectFailedException(String message) {
		super(message);
	}
}
