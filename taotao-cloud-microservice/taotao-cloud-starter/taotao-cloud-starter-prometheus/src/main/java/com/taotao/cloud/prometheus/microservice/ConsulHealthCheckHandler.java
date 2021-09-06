//package com.taotao.cloud.prometheus.microservice.components;
//
//import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
//import com.taotao.cloud.prometheus.microservice.interfaces.HealthCheckHandler;
//import com.taotao.cloud.prometheus.properties.servicemonitor.ServiceCheck;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.cloud.client.ServiceInstance;
//
//
//public class ConsulHealthCheckHandler implements HealthCheckHandler, InitializingBean {
//
//	private final NacosDiscoveryClient consulClient;
//
//	private final Map<String, Check> healthChekCacheMap = new ConcurrentHashMap<>();
//
//	public ConsulHealthCheckHandler(NacosDiscoveryClient consulClient) {
//		this.consulClient = consulClient;
//	}
//
//	@Override
//	public synchronized boolean isHealthy(ServiceInstance serviceInstance, ServiceCheck serviceCheck) {
//		if (serviceInstance.getServiceId().equals("consul"))
//			return true;
//		Check checkCache = healthChekCacheMap.remove(serviceInstance.getInstanceId());
//		if (checkCache == null) {
//			refresh();
//			checkCache = healthChekCacheMap.remove(serviceInstance.getInstanceId());
//		}
//		return checkCache != null && checkCache.getStatus() == CheckStatus.PASSING;
//	}
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		refresh();
//	}
//
//	private void refresh() {
//		List<Check> list = consulClient.getInstances(QueryParams.DEFAULT).getValue();
//		list.forEach(x -> healthChekCacheMap.put(x.getServiceId(), x));
//	}
//}
