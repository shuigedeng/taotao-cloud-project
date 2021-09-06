package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.model.ServiceHealth;
import com.taotao.cloud.prometheus.model.ServiceStatus;
import com.taotao.cloud.prometheus.properties.ServiceCheck;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;


public class ServiceCheckTask implements Runnable {

	private final String serviceId;

	private final ServiceCheck serviceCheck;

	private final DiscoveryClient discoveryClient;

	private final HealthCheckHandler healthCheckHandler;

	private final ApplicationEventPublisher applicationEventPublisher;

	private Map<String, ServiceInstance> servicesMap = new HashMap<String, ServiceInstance>();

	public ServiceCheckTask(String serviceId, ServiceCheck serviceCheck, DiscoveryClient discoveryClient,
			HealthCheckHandler healthCheckHandler, ApplicationEventPublisher applicationEventPublisher) {
		this.serviceId = serviceId;
		this.serviceCheck = serviceCheck;
		this.discoveryClient = discoveryClient;
		this.healthCheckHandler = healthCheckHandler;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public ServiceCheck getServiceCheck() {
		return serviceCheck;
	}

	public Map<String, ServiceInstance> getServicesMap() {
		return servicesMap;
	}

	public void setServicesMap(Map<String, ServiceInstance> servicesMap) {
		this.servicesMap = servicesMap;
	}

	@Override
	public void run() {
		freshInstance();
		List<ServiceHealth> list = new ArrayList<ServiceHealth>(servicesMap.size());
		servicesMap.forEach((x, y) -> {
			if (!healthCheckHandler.isHealthy(y, serviceCheck))
				list.add(new ServiceHealth(y.getInstanceId(), ServiceStatus.DOWN));
		});
		if (list.size() > 0) {
			applicationEventPublisher.publishEvent(new ServiceInstanceUnhealthyEvent(this, serviceId, list));
		}
	}

	public void freshInstance() {
		List<ServiceInstance> list = discoveryClient.getInstances(serviceId);
		servicesMap.clear();
		list.forEach(x -> servicesMap.put(x.getInstanceId(), x));
	}

	@Override
	public String toString() {
		return new StringBuilder().append("ServiceCheckTask:").append(serviceId).append("-->")
				.append(serviceCheck.toString()).append("\n").toString();
	}

}
