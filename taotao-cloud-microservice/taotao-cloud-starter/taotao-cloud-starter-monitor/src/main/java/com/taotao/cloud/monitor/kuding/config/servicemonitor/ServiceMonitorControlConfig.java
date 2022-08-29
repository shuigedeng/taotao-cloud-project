package com.taotao.cloud.monitor.kuding.config.servicemonitor;

import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnServiceMonitor;
import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.microservice.control.ServiceCheckControl;
import com.taotao.cloud.monitor.kuding.microservice.control.ServiceExistControl;
import com.taotao.cloud.monitor.kuding.microservice.control.ServiceNoticeControl;
import com.taotao.cloud.monitor.kuding.microservice.interfaces.HealthCheckHandler;
import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.monitor.kuding.pojos.notice.ServiceCheckNotice;
import com.taotao.cloud.monitor.kuding.properties.NoticeProperties;
import com.taotao.cloud.monitor.kuding.properties.servicemonitor.ServiceMonitorProperties;
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
	 * 服务检查控制器
	 *
	 * @param healthCheckHandler
	 * @return
	 */
	@Bean
	public ServiceCheckControl serviceCheckControl(HealthCheckHandler healthCheckHandler) {
		ServiceCheckControl checkControl = new ServiceCheckControl(promethuesMicroServiceScheduler,
			serviceMonitorProperties, discoveryClient, applicationEventPublisher,
			healthCheckHandler);
		return checkControl;
	}

	/**
	 * 服务通知控制器
	 *
	 * @param noticeProperties
	 * @param serviceCheckNoticeRepository
	 * @param noticeSendComponents
	 * @param reportedFilterHandler
	 * @return
	 */
	@Bean
	public ServiceNoticeControl serviceNoticeControl(
		NoticeProperties noticeProperties,
		ServiceNoticeRepository serviceNoticeRepository,
		List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents) {
		return new ServiceNoticeControl(
			serviceMonitorProperties,
			noticeProperties, promethuesMicroServiceScheduler, noticeSendComponents,
			serviceNoticeRepository);
	}

	/**
	 * 服务存在性控制器
	 *
	 * @return
	 */
	@Bean
	public ServiceExistControl serviceExistControl() {
		return new ServiceExistControl(
			promethuesMicroServiceScheduler,
			discoveryClient, applicationEventPublisher, serviceMonitorProperties);
	}
}
