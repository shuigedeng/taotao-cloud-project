package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscovery {
	private final List<ServerAddress> servers = new ArrayList<>();
	private final LoadBalancer loadBalancer;

	public ServiceDiscovery() {
		// TODO 默认用随机策略（Random），可以
		this.loadBalancer = SpiExtensionFactory.getExtension("random", LoadBalancer.class);
//        this.loadBalancer = SpiExtensionFactory.getExtension("round_robin", LoadBalancer.class);
	}

	public ServiceDiscovery(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	// 添加或更新服务地址
	public void update(List<ServerAddress> newServers) {
		servers.clear();
		servers.addAll(newServers);
	}

	// 获取一个可用的服务实例
	public ServerAddress selector() {
		return loadBalancer.select(servers);
	}

	// 标记某个服务不可用（例如健康检查失败）
	public void markServerDown(String host, int port) {
		servers.stream()
			.filter(s -> s.getHost().equals(host) && s.getPort() == port)
			.findFirst()
			.ifPresent(s -> s.setActive(false));
	}
}
