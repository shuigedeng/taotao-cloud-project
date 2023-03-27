package com.taotao.cloud.rpc.common.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.exception.ServiceNotFoundException;
import java.util.List;
import java.util.Random;

/**
 * 随机选择
 */
public class RandomLoadBalancer implements LoadBalancer {

	@Override
	public Instance selectService(List<Instance> instances) throws RpcException {
		if (instances.size() == 0) {
			throw new ServiceNotFoundException(
				"service instances size is zero, can't provide service! please start server first!");
		}
		return instances.get(new Random().nextInt(instances.size()));
	}

	@Override
	public String selectNode(String[] nodes) throws RpcException {
		if (nodes.length == 0) {
			throw new ServiceNotFoundException(
				"service instances size is zero, can't provide service! please start server first!");
		}
		return nodes[new Random().nextInt(nodes.length)];
	}

}
