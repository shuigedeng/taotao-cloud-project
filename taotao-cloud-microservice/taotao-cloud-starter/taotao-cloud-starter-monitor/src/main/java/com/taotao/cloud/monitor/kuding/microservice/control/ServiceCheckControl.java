package com.taotao.cloud.monitor.kuding.microservice.control;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.taotao.cloud.monitor.kuding.microservice.interfaces.HealthCheckHandler;
import com.taotao.cloud.monitor.kuding.microservice.task.ServiceCheckTask;
import com.taotao.cloud.monitor.kuding.properties.servicemonitor.ServiceCheck;
import com.taotao.cloud.monitor.kuding.properties.servicemonitor.ServiceMonitorProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;


public class ServiceCheckControl implements DisposableBean {

	private final TaskScheduler taskScheduler;

	private final Set<String> checkServices = Collections.synchronizedSet(new HashSet<>());

	Map<String, ScheduledFuture<?>> taskResultMap = new ConcurrentHashMap<String, ScheduledFuture<?>>();

	private final DiscoveryClient discoveryClient;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final HealthCheckHandler healthCheckHandler;

	private final Log logger = LogFactory.getLog(ServiceCheckControl.class);

	public ServiceCheckControl(TaskScheduler taskScheduler, ServiceMonitorProperties serviceMonitorProperties,
			DiscoveryClient discoveryClient, ApplicationEventPublisher applicationEventPublisher,
			HealthCheckHandler healthCheckHandler) {
		this.taskScheduler = taskScheduler;
		this.discoveryClient = discoveryClient;
		this.applicationEventPublisher = applicationEventPublisher;
		this.healthCheckHandler = healthCheckHandler;
	}

	@Override
	public void destroy() throws Exception {
		taskResultMap.forEach((x, y) -> {
			y.cancel(false);
		});
	}

	public synchronized void add(@NonNull String service, @NonNull ServiceCheck serviceCheck) {
		if (checkServices.contains(service)) {
			logger.info("there is a task running: " + service);
			return;
		}
		ServiceCheckTask serviceCheckTask = new ServiceCheckTask(service, serviceCheck, discoveryClient,
				healthCheckHandler, applicationEventPublisher);
		ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(serviceCheckTask,
				serviceCheck.getCheckInterval());
		taskResultMap.put(service, scheduledFuture);
		checkServices.add(service);
	}

	public Map<String, ScheduledFuture<?>> getTaskResultMap() {
		return taskResultMap;
	}

	public void setTaskResultMap(Map<String, ScheduledFuture<?>> taskResultMap) {
		this.taskResultMap = taskResultMap;
	}

	/**
	 * @return the taskScheduler
	 */
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	/**
	 * @return the discoveryClient
	 */
	public DiscoveryClient getDiscoveryClient() {
		return discoveryClient;
	}

	/**
	 * @return the applicationEventPublisher
	 */
	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	/**
	 * @return the healthCheckHandler
	 */
	public HealthCheckHandler getHealthCheckHandler() {
		return healthCheckHandler;
	}

}
