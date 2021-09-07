package com.taotao.cloud.prometheus.microservice;

import java.util.Set;

import org.springframework.context.ApplicationListener;

import com.kuding.microservice.interfaces.ServiceNoticeRepository;
import com.kuding.pojos.servicemonitor.ServiceInstanceLackProblem;

public class ServiceInstanceLackEventListener implements ApplicationListener<ServiceInstanceLackEvent> {

	private final ServiceNoticeRepository serviceNoticeRepository;

	public ServiceInstanceLackEventListener(ServiceNoticeRepository serviceNoticeRepository) {
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void onApplicationEvent(ServiceInstanceLackEvent event) {
		Set<String> existedInstances = event.getInstanceIds();
		int lackCount = event.getServiceCount() - existedInstances.size();
		if (lackCount > 0 && lackCount == event.getServiceCount())
			serviceNoticeRepository.addLackServices(event.getServiceName());
		else if (lackCount > 0) {
			serviceNoticeRepository.addServiceLackProblem(
					new ServiceInstanceLackProblem(event.getServiceName(), event.getInstanceIds(), lackCount));
		}
	}

}
