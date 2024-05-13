package com.taotao.cloud.rpc.core.net;


import com.taotao.cloud.rpc.common.exception.RpcException;

public interface RpcServer {

	void start();

	<T> void publishService(T service, String serviceClass) throws RpcException;

	<T> void publishService(T service, String groupName, String serviceClass) throws RpcException;

	/**
	 * @param fullName         全类名
	 * @param simpleName       忽略包类名
	 * @param firstLowCaseName 首字母小写类名
	 * @param clazz            Class 类，可用于发射
	 * @return
	 * @throws Exception
	 */
	Object newInstance(String fullName, String simpleName, String firstLowCaseName, Class<?> clazz)
		throws Exception;

}
