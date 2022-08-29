package com.taotao.cloud.monitor.kuding.config.servicemonitor;

import com.taotao.cloud.monitor.kuding.microservice.control.ServiceCheckControl;
import com.taotao.cloud.monitor.kuding.microservice.events.ServiceDiscoveredListener;
import com.taotao.cloud.monitor.kuding.microservice.events.ServiceInstanceLackEventListener;
import com.taotao.cloud.monitor.kuding.microservice.events.ServiceInstanceUnhealthyEventListener;
import com.taotao.cloud.monitor.kuding.microservice.events.ServiceLostEventListener;
import com.taotao.cloud.monitor.kuding.properties.servicemonitor.ServiceMonitorProperties;
import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
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
		return new ServiceDiscoveredListener(serviceCheckControl,
				serviceMonitorProperties.getMonitorServices(), discoveryClient, publisher);
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
		return new ServiceLostEventListener(serviceNoticeRepository);
	}
}
