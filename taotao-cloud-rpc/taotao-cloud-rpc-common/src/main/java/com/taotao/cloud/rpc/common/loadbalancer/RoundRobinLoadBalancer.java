package com.taotao.cloud.rpc.common.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.exception.ServiceNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询选择
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

	private final AtomicLong idx = new AtomicLong();

	@Override
	public Instance selectService(List<Instance> instances) throws RpcException {
		if (instances.isEmpty()) {
			throw new ServiceNotFoundException(
				"service instances size is zero, can't provide service! please start server first!");
		}
		int num = (int) (idx.getAndIncrement() % (long) instances.size());
		return instances.get(num < 0 ? -num : num);
	}

	@Override
	public String selectNode(String[] nodes) throws RpcException {
		if (nodes.length == 0) {
			throw new ServiceNotFoundException(
				"service instances size is zero, can't provide service! please start server first!");
		}
		int num = (int) (idx.getAndIncrement() % (long) nodes.length);
		return nodes[num < 0 ? -num : num];

	}

}
