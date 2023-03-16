package com.taotao.cloud.rpc.common.exception;

/**
 * 服务未找到错误
 */
public class ServiceNotFoundException extends RpcException {

	public ServiceNotFoundException() {
	}

	public ServiceNotFoundException(String message) {
		super(message);
	}
}
