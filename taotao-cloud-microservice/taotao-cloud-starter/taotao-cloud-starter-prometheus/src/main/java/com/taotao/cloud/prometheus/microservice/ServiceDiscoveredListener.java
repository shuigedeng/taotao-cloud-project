package com.taotao.cloud.prometheus.microservice;

import static java.util.stream.Collectors.toSet;

import com.taotao.cloud.prometheus.properties.ServiceCheckProperties;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;


public class ServiceDiscoveredListener implements ApplicationListener<ServiceDiscoveredEvent> {

	private final ServiceCheckControl serviceCheckControl;

	private final DiscoveryClient discoveryClient;

	private final Map<String, ServiceCheckProperties> map;

	private final ServiceCheckProperties defaultServiceCheckProperties;

	private final ApplicationEventPublisher applicationEventPublisher;

	public ServiceDiscoveredListener(ServiceCheckControl serviceCheckControl, Map<String, ServiceCheckProperties> map,
			DiscoveryClient discoveryClient, ApplicationEventPublisher applicationEventPublisher) {
		this.serviceCheckControl = serviceCheckControl;
		this.discoveryClient = discoveryClient;
		this.map = map;
		defaultServiceCheckProperties = map.remove("default");
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 *
	 * 处理发现的微服务
	 */
	@Override
	public void onApplicationEvent(ServiceDiscoveredEvent event) {
		Set<String> notHaveServcie = map.keySet().stream().filter(x -> !event.getAllService().contains(x))
				.collect(toSet());
		event.getLackServices().addAll(notHaveServcie);
		applicationEventPublisher.publishEvent(new ServiceLostEvent(this, event.getLackServices()));
		event.getAllService().forEach(x -> {
			List<ServiceInstance> list = discoveryClient.getInstances(x);
			ServiceCheckProperties serviceCheckProperties = map.getOrDefault(x,
				defaultServiceCheckProperties);
			int serviceCount = serviceCheckProperties == defaultServiceCheckProperties ? list.size() : serviceCheckProperties.getServiceCount();
			if (list.size() < serviceCount)
				applicationEventPublisher.publishEvent(new ServiceInstanceLackEvent(this, x, serviceCount,
						list.stream().map(y -> y.getInstanceId()).collect(toSet())));
			if (event.getAdditionalServices().contains(x)) {
				serviceCheckControl.add(x, serviceCheckProperties);
			}
		});
	}

}
