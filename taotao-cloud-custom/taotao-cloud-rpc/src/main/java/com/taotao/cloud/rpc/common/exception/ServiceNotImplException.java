package com.taotao.cloud.rpc.common.exception;

/**
 * 服务未实现一个接口错误
 */
public class ServiceNotImplException extends RpcException {

	public ServiceNotImplException() {
		super();
	}

	public ServiceNotImplException(String message) {
		super(message);
	}

}
