package com.taotao.cloud.monitor.kuding.microservice.interfaces;

import com.taotao.cloud.monitor.kuding.properties.servicemonitor.ServiceCheck;
import org.springframework.cloud.client.ServiceInstance;


@FunctionalInterface
public interface HealthCheckHandler {

	public boolean isHealthy(ServiceInstance serviceInstance, ServiceCheck serviceCheck);

}
