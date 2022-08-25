package com.taotao.cloud.monitor.kuding.microservice.task;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.taotao.cloud.monitor.kuding.microservice.events.ServiceDiscoveredEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;


public class ServiceExistCheckTask implements Runnable {

	private final Set<String> allService = new TreeSet<String>();

	private final DiscoveryClient discoveryClient;

	private final ApplicationEventPublisher applicationEventPublisher;

	private boolean autoDetected = false;

	private final Log logger = LogFactory.getLog(ServiceExistCheckTask.class);

	public ServiceExistCheckTask(DiscoveryClient discoveryClient, ApplicationEventPublisher applicationEventPublisher,
			boolean autoDetected) {
		this.discoveryClient = discoveryClient;
		this.applicationEventPublisher = applicationEventPublisher;
		this.autoDetected = autoDetected;
	}

	public boolean isAuthDetected() {
		return autoDetected;
	}

	public Set<String> getAllService() {
		return allService;
	}

	public void setAuthDetected(boolean authDetected) {
		this.autoDetected = authDetected;
	}

	public DiscoveryClient getDiscoveryClient() {
		return discoveryClient;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	@Override
	public void run() {
		Set<String> existedServices = new HashSet<>(discoveryClient.getServices());
		Set<String> lackServices = allService.stream().filter(x -> !existedServices.contains(x)).collect(toSet());
		Set<String> additionalServices = Collections.emptySet();
		if (autoDetected) {
			additionalServices = existedServices.stream().filter(x -> !allService.contains(x)).collect(toSet());
			if (additionalServices.size() > 0)
				logger.info("service detected: " + additionalServices);
			allService.addAll(additionalServices);
		}
		applicationEventPublisher.publishEvent(
				new ServiceDiscoveredEvent(this, lackServices, additionalServices, existedServices.size(), allService));
	}
}
