package com.taotao.cloud.rpc.core.registry;


import com.taotao.cloud.rpc.common.exception.RpcException;
import java.net.InetSocketAddress;

/**
 * 注册表公共接口
 */
public interface ServiceRegistry {

	void register(String serviceName, InetSocketAddress inetSocketAddress) throws RpcException;

	void register(String serviceName, String groupName, InetSocketAddress inetSocketAddress)
		throws RpcException;
}
