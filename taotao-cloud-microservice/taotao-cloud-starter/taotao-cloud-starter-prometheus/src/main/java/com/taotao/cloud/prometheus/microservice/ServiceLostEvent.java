package com.taotao.cloud.prometheus.microservice;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

public class ServiceLostEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final Set<String> serviceNames;

	public ServiceLostEvent(Object source, Set<String> serviceNames) {
		super(source);
		this.serviceNames = serviceNames;
	}

	public Set<String> getServiceNames() {
		return serviceNames;
	}

}
