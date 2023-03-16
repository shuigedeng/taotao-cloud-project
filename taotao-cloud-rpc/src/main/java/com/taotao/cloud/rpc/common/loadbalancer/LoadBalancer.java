package com.taotao.cloud.rpc.common.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.rpc.common.exception.RpcException;
import java.util.List;

/**
 * 负载均衡 接口
 */
public interface LoadBalancer {

	Instance selectService(List<Instance> instances) throws RpcException;

	String selectNode(String[] nodes) throws RpcException;

	static LoadBalancer getByCode(int code) {
		switch (code) {
			case 0:
				return new RandomLoadBalancer();
			case 1:
				return new RoundRobinLoadBalancer();
			default:
				return null;
		}
	}
}
