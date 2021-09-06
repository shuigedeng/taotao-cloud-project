package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.properties.ServiceMonitorProperties;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;


public class ServiceExistControl implements SmartInitializingSingleton, DisposableBean {

	private final TaskScheduler taskScheduler;

	private final DiscoveryClient discoveryClient;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final ServiceMonitorProperties serviceMonitorProperties;

	private ScheduledFuture<?> scheduledResult;

	public ServiceExistControl(TaskScheduler taskScheduler, DiscoveryClient discoveryClient,
			ApplicationEventPublisher applicationEventPublisher, ServiceMonitorProperties serviceMonitorProperties) {
		this.taskScheduler = taskScheduler;
		this.discoveryClient = discoveryClient;
		this.applicationEventPublisher = applicationEventPublisher;
		this.serviceMonitorProperties = serviceMonitorProperties;
	}

	@Override
	public void destroy() throws Exception {
		scheduledResult.cancel(false);
	}

	@Override
	public void afterSingletonsInstantiated() {
		ServiceExistCheckTask serviceExistCheckTask = new ServiceExistCheckTask(discoveryClient,
				applicationEventPublisher, serviceMonitorProperties.isAutoDiscovery());
		scheduledResult = taskScheduler.scheduleAtFixedRate(serviceExistCheckTask,
				serviceMonitorProperties.getServiceExistCheckInterval());
	}

	public DiscoveryClient getDiscoveryClient() {
		return discoveryClient;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public ServiceMonitorProperties getServiceMonitorProperties() {
		return serviceMonitorProperties;
	}

}
