package com.taotao.cloud.monitor.kuding.microservice.events;

import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.ServiceHealthProblem;
import org.springframework.context.ApplicationListener;


public class ServiceInstanceUnhealthyEventListener implements ApplicationListener<ServiceInstanceUnhealthyEvent> {

	private final ServiceNoticeRepository serviceNoticeRepository;

	public ServiceInstanceUnhealthyEventListener(ServiceNoticeRepository serviceNoticeRepository) {
		super();
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void onApplicationEvent(ServiceInstanceUnhealthyEvent event) {
		serviceNoticeRepository.addServiceHealthProblem(new ServiceHealthProblem(event));
	}

}
