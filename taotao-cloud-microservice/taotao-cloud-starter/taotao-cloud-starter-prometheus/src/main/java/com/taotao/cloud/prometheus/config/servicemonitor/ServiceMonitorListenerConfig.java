package com.taotao.cloud.prometheus.config.servicemonitor;

import com.taotao.cloud.prometheus.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.prometheus.microservice.control.ServiceCheckControl;
import com.taotao.cloud.prometheus.microservice.events.ServiceDiscoveredListener;
import com.taotao.cloud.prometheus.microservice.events.ServiceInstanceLackEventListener;
import com.taotao.cloud.prometheus.microservice.events.ServiceInstanceUnhealthyEventListener;
import com.taotao.cloud.prometheus.microservice.events.ServiceLostEventListener;
import com.taotao.cloud.prometheus.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.prometheus.properties.servicemonitor.ServiceMonitorProperties;
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
