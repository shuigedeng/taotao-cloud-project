package com.taotao.cloud.monitor.kuding.microservice.events;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

public class ServiceDiscoveredEvent extends ApplicationEvent {

	private static final long serialVersionUID = -1706134334761112080L;

	private final Set<String> lackServices;

	private final Set<String> additionalServices;

	private final Set<String> allService;

	private final int totalCount;

	/**
	 * @param source
	 * @param lackServices
	 * @param additionalServices
	 * @param totalCount
	 */
	public ServiceDiscoveredEvent(Object source, Set<String> lackServices, Set<String> additionalServices,
			int totalCount, Set<String> allService) {
		super(source);
		this.lackServices = lackServices;
		this.additionalServices = additionalServices;
		this.allService = allService;
		this.totalCount = totalCount;
	}

	public Set<String> getLackServices() {
		return lackServices;
	}

	public Set<String> getAdditionalServices() {
		return additionalServices;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public Set<String> getAllService() {
		return allService;
	}

}
