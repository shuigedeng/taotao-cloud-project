package com.taotao.cloud.prometheus.configuration;

import com.taotao.cloud.prometheus.annotation.ConditionalOnServiceMonitor;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.microservice.ServiceCheckControl;
import com.taotao.cloud.prometheus.microservice.ServiceExistControl;
import com.taotao.cloud.prometheus.microservice.ServiceNoticeControl;
import com.taotao.cloud.prometheus.microservice.interfaces.HealthCheckHandler;
import com.taotao.cloud.prometheus.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.prometheus.model.ServiceCheckNotice;
import com.taotao.cloud.prometheus.properties.PromethreusNoticeProperties;
import com.taotao.cloud.prometheus.properties.ServiceMonitorProperties;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;


@Configuration
@ConditionalOnServiceMonitor
public class ServiceMonitorControlConfig {

	@Autowired
	private ServiceMonitorProperties serviceMonitorProperties;

	@Autowired
	private TaskScheduler promethuesMicroServiceScheduler;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private DiscoveryClient discoveryClient;

	/**
	 * 
	 * 服务检查控制器
	 * 
	 * @param healthCheckHandler
	 * @return
	 */
	@Bean
	public ServiceCheckControl serviceCheckControl(HealthCheckHandler healthCheckHandler) {
		ServiceCheckControl checkControl = new ServiceCheckControl(promethuesMicroServiceScheduler,
				serviceMonitorProperties, discoveryClient, applicationEventPublisher, healthCheckHandler);
		return checkControl;
	}

	/**
	 * 
	 * 服务通知控制器
	 * 
	 * @param promethreusNoticeProperties
	 * @param serviceCheckNoticeRepository
	 * @param noticeSendComponents
	 * @param reportedFilterHandler
	 * @return
	 */
	@Bean
	public ServiceNoticeControl serviceNoticeControl(
		PromethreusNoticeProperties promethreusNoticeProperties,
			ServiceNoticeRepository serviceNoticeRepository,
			List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents) {
		ServiceNoticeControl serviceNoticeControl = new ServiceNoticeControl(serviceMonitorProperties,
				promethreusNoticeProperties, promethuesMicroServiceScheduler, noticeSendComponents,
				serviceNoticeRepository);
		return serviceNoticeControl;
	}

	/**
	 * 服务存在性控制器
	 * 
	 * @return
	 */
	@Bean
	public ServiceExistControl serviceExistControl() {
		ServiceExistControl serviceExistControl = new ServiceExistControl(promethuesMicroServiceScheduler,
				discoveryClient, applicationEventPublisher, serviceMonitorProperties);
		return serviceExistControl;
	}
}
