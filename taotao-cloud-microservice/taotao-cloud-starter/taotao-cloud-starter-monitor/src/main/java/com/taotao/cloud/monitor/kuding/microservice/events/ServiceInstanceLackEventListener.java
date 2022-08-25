package com.taotao.cloud.monitor.kuding.microservice.events;

import java.util.Set;

import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.ServiceInstanceLackProblem;
import org.springframework.context.ApplicationListener;


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
