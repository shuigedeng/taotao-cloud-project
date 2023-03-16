package com.taotao.cloud.rpc.core.discovery;

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.loadbalancer.LoadBalancer;
import java.net.InetSocketAddress;

/**
 * 服务发现 接口
 */
public abstract class ServiceDiscovery {

	protected LoadBalancer loadBalancer;

	public ServiceDiscovery() {
	}

	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public abstract InetSocketAddress lookupService(String serviceName) throws RpcException;

	public abstract InetSocketAddress lookupService(String serviceName, String groupName)
		throws RpcException;
}
