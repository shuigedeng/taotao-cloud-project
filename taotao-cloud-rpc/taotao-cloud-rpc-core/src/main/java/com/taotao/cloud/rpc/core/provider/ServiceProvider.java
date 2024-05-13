package com.taotao.cloud.rpc.core.provider;

import com.taotao.cloud.rpc.common.exception.RpcException;

/**
 * 服务提供者 公共接口
 */
public interface ServiceProvider {

	// 泛型方法，故而才可以有参数类型 T
	<T> void addServiceProvider(T service, String ServiceName) throws RpcException;

	Object getServiceProvider(String serviceName) throws RpcException;

}
