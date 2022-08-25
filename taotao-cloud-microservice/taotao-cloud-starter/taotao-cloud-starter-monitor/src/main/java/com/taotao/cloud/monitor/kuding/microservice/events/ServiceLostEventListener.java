package com.taotao.cloud.monitor.kuding.microservice.events;

import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
import org.springframework.context.ApplicationListener;


public class ServiceLostEventListener implements ApplicationListener<ServiceLostEvent> {

	private final ServiceNoticeRepository serviceNoticeRepository;

	public ServiceLostEventListener(ServiceNoticeRepository serviceNoticeRepository) {
		super();
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void onApplicationEvent(ServiceLostEvent event) {
		serviceNoticeRepository.addLackServices(event.getServiceNames());
	}

}
