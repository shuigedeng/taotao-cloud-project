package com.taotao.cloud.rpc.core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.taotao.cloud.rpc.common.exception.RegisterFailedException;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.util.NacosUtils;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

	/**
	 * 以默认组名”DEFAULT_GROUP“, 注册服务名对应的服务套接字地址
	 *
	 * @param serviceName       服务名
	 * @param inetSocketAddress 套接字地址
	 * @throws RpcException
	 */
	@Override
	public void register(String serviceName, InetSocketAddress inetSocketAddress)
		throws RpcException {
		try {
			NacosUtils.registerService(serviceName, inetSocketAddress);
		} catch (NacosException e) {
			log.error("Failed to register service, {}", e.getMessage());
			throw new RegisterFailedException("Failed to register service Exception");
		}
	}

	/**
	 * 注册组名下服务名对应的服务套接字地址
	 *
	 * @param serviceName       服务名
	 * @param groupName         组名
	 * @param inetSocketAddress 套接字地址
	 * @throws RpcException
	 */
	@Override
	public void register(String serviceName, String groupName, InetSocketAddress inetSocketAddress)
		throws RpcException {
		try {
			NacosUtils.registerService(serviceName, groupName, inetSocketAddress);
		} catch (NacosException e) {
			log.error("Failed to register servicem {}", e.getMessage());
			throw new RegisterFailedException("Failed to register service Exception");
		}
	}

}
