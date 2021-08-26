package com.taotao.cloud.prometheus.config.servicemonitor;

import com.taotao.cloud.prometheus.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.prometheus.microservice.components.ConsulHealthCheckHandler;
import com.taotao.cloud.prometheus.microservice.interfaces.HealthCheckHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.consul.discovery.ConditionalOnConsulDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnServiceMonitor
@ConditionalOnConsulDiscoveryEnabled
public class ConsulHealthCheckHandlerConfig {

	@Bean
	@ConditionalOnMissingBean
	public HealthCheckHandler healthCheckHandler(ConsulClient consulClient) {
		HealthCheckHandler handler = new ConsulHealthCheckHandler(consulClient);
		return handler;
	}
}
