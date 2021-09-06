package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.prometheus.model.ServiceCheckNotice;
import com.taotao.cloud.prometheus.properties.PromethreusNoticeProperties;
import com.taotao.cloud.prometheus.properties.ServiceMonitorProperties;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;


public class ServiceNoticeControl implements SmartInitializingSingleton, DisposableBean {

	private final Log logger = LogFactory.getLog(ServiceNoticeControl.class);

	private final ServiceMonitorProperties serviceMonitorProperties;

	private final PromethreusNoticeProperties promethreusNoticeProperties;

	private final TaskScheduler taskScheduler;

	private final ServiceNoticeRepository serviceNoticeRepository;

	private final List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents;

	private ScheduledFuture<?> result;

	/**
	 * @param serviceMonitorProperties
	 * @param promethreusNoticeProperties
	 * @param taskScheduler
	 */
	public ServiceNoticeControl(ServiceMonitorProperties serviceMonitorProperties,
			PromethreusNoticeProperties promethreusNoticeProperties, TaskScheduler taskScheduler,
			List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents,
			ServiceNoticeRepository serviceNoticeRepository) {
		this.serviceMonitorProperties = serviceMonitorProperties;
		this.promethreusNoticeProperties = promethreusNoticeProperties;
		this.taskScheduler = taskScheduler;
		this.serviceNoticeRepository = serviceNoticeRepository;
		this.noticeSendComponents = noticeSendComponents;
	}

	public ServiceMonitorProperties getServiceMonitorProperties() {
		return serviceMonitorProperties;
	}

	/**
	 * @return the result
	 */
	public ScheduledFuture<?> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ScheduledFuture<?> result) {
		this.result = result;
	}

	/**
	 * @return the taskScheduler
	 */
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	@Override
	public void destroy() throws Exception {
		result.cancel(false);
	}

	@Override
	public void afterSingletonsInstantiated() {
		logger.debug("开启通知任务");
		ServiceNoticeTask serviceNoticeTask = new ServiceNoticeTask(noticeSendComponents, promethreusNoticeProperties,
				serviceNoticeRepository);
		PeriodicTrigger trigger = new PeriodicTrigger(
				serviceMonitorProperties.getServiceCheckNoticeInterval().toMillis());
		trigger.setInitialDelay(30000);
		result = taskScheduler.schedule(serviceNoticeTask, trigger);
	}
}
