package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.annotation.ConditionalOnServiceMonitor;
import com.taotao.cloud.prometheus.microservice.ServiceCheckControl;
import com.taotao.cloud.prometheus.microservice.ServiceDiscoveredListener;
import com.taotao.cloud.prometheus.microservice.ServiceInstanceLackEventListener;
import com.taotao.cloud.prometheus.microservice.ServiceInstanceUnhealthyEventListener;
import com.taotao.cloud.prometheus.microservice.ServiceLostEventListener;
import com.taotao.cloud.prometheus.microservice.ServiceNoticeRepository;
import com.taotao.cloud.prometheus.properties.ServiceMonitorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnServiceMonitor
public class ServiceMonitorListenerConfig {

	@Autowired
	private ServiceMonitorProperties serviceMonitorProperties;

	@Bean
	public ServiceDiscoveredListener serviceExistedListener(ServiceCheckControl serviceCheckControl,
			DiscoveryClient discoveryClient, ApplicationEventPublisher publisher) {
		ServiceDiscoveredListener existedListener = new ServiceDiscoveredListener(serviceCheckControl,
				serviceMonitorProperties.getMonitorServices(), discoveryClient, publisher);
		return existedListener;
	}

	@Bean
	public ServiceInstanceLackEventListener serviceInstanceLackEventListener(
			ServiceNoticeRepository serviceNoticeRepository) {
		return new ServiceInstanceLackEventListener(serviceNoticeRepository);
	}

	@Bean
	public ServiceInstanceUnhealthyEventListener serviceInstanceUnhealthyEventListener(
			ServiceNoticeRepository serviceNoticeRepository) {
		return new ServiceInstanceUnhealthyEventListener(serviceNoticeRepository);
	}

	@Bean
	public ServiceLostEventListener serviceLostEventListener(ServiceNoticeRepository serviceNoticeRepository) {
		ServiceLostEventListener serviceLostEventListener = new ServiceLostEventListener(serviceNoticeRepository);
		return serviceLostEventListener;
	}
}
