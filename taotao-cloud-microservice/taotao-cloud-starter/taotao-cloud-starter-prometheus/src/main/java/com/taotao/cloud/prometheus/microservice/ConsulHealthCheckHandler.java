//package com.taotao.cloud.prometheus.microservice;
//
//import com.taotao.cloud.prometheus.properties.ServiceCheckProperties;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.cloud.client.ServiceInstance;
//
//import com.ecwid.consul.v1.ConsulClient;
//import com.ecwid.consul.v1.QueryParams;
//import com.ecwid.consul.v1.health.model.Check;
//import com.ecwid.consul.v1.health.model.Check.CheckStatus;
//
//public class ConsulHealthCheckHandler implements HealthCheckHandler, InitializingBean {
//
//	private final ConsulClient consulClient;
//
//	private final Map<String, Check> healthChekCacheMap = new ConcurrentHashMap<>();
//
//	public ConsulHealthCheckHandler(ConsulClient consulClient) {
//		this.consulClient = consulClient;
//	}
//
//	@Override
//	public synchronized boolean isHealthy(ServiceInstance serviceInstance, ServiceCheckProperties serviceCheckProperties) {
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
//		List<Check> list = consulClient.getHealthChecksState(QueryParams.DEFAULT).getValue();
//		list.forEach(x -> healthChekCacheMap.put(x.getServiceId(), x));
//	}
//}
