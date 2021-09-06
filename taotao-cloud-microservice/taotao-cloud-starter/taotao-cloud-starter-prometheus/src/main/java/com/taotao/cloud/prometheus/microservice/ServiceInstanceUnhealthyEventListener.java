package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.model.ServiceHealthProblem;
import org.springframework.context.ApplicationListener;


public class ServiceInstanceUnhealthyEventListener implements ApplicationListener<ServiceInstanceUnhealthyEvent> {

	private final ServiceNoticeRepository serviceNoticeRepository;

//	private final Log logger = LogFactory.getLog(ServiceInstanceUnhealthyEvent.class);

	public ServiceInstanceUnhealthyEventListener(ServiceNoticeRepository serviceNoticeRepository) {
		super();
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void onApplicationEvent(ServiceInstanceUnhealthyEvent event) {
		serviceNoticeRepository.addServiceHealthProblem(new ServiceHealthProblem(event));

	}

}
