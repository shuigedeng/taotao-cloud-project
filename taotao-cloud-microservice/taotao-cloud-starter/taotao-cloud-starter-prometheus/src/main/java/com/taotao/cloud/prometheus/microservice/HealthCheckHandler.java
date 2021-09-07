package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.properties.ServiceCheckProperties;
import org.springframework.cloud.client.ServiceInstance;


@FunctionalInterface
public interface HealthCheckHandler {

	public boolean isHealthy(ServiceInstance serviceInstance, ServiceCheckProperties serviceCheckProperties);

}
