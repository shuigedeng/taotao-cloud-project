package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.properties.ServiceCheck;
import org.springframework.cloud.client.ServiceInstance;


@FunctionalInterface
public interface HealthCheckHandler {

	public boolean isHealthy(ServiceInstance serviceInstance, ServiceCheck serviceCheck);

}
