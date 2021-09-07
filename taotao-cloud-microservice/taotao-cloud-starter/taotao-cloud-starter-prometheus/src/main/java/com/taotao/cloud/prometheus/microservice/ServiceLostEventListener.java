package com.taotao.cloud.prometheus.microservice;

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
