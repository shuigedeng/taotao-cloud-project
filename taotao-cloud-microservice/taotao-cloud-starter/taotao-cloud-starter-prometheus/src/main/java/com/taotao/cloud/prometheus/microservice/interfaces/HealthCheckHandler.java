package com.taotao.cloud.prometheus.microservice.interfaces;

import com.taotao.cloud.prometheus.properties.servicemonitor.ServiceCheck;
import org.springframework.cloud.client.ServiceInstance;


@FunctionalInterface
public interface HealthCheckHandler {

	public boolean isHealthy(ServiceInstance serviceInstance, ServiceCheck serviceCheck);

}
