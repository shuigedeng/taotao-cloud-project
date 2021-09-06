//package com.taotao.cloud.prometheus.config.servicemonitor;
//
//import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
//import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
//import com.taotao.cloud.prometheus.config.annos.ConditionalOnServiceMonitor;
//import com.taotao.cloud.prometheus.microservice.components.ConsulHealthCheckHandler;
//import com.taotao.cloud.prometheus.microservice.interfaces.HealthCheckHandler;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//@ConditionalOnServiceMonitor
//@ConditionalOnNacosDiscoveryEnabled
//public class ConsulHealthCheckHandlerConfig {
//
//	@Bean
//	@ConditionalOnMissingBean
//	public HealthCheckHandler healthCheckHandler(NacosDiscoveryClient consulClient) {
//		HealthCheckHandler handler = new ConsulHealthCheckHandler(consulClient);
//		return handler;
//	}
//}
