package com.taotao.cloud.prometheus.microservice;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.MicroServiceReport;
import com.taotao.cloud.prometheus.model.ServiceCheckNotice;
import com.taotao.cloud.prometheus.properties.PromethreusNoticeProperties;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceNoticeTask implements Runnable {

	private final List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents;

	private final PromethreusNoticeProperties promethreusNoticeProperties;

	private final Log logger = LogFactory.getLog(ServiceNoticeTask.class);

	private final ServiceNoticeRepository serviceNoticeRepository;

	/**
	 * @param promethreusNoticeProperties
	 */
	public ServiceNoticeTask(List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents,
			PromethreusNoticeProperties promethreusNoticeProperties, ServiceNoticeRepository serviceNoticeRepository) {
		this.noticeSendComponents = noticeSendComponents;
		this.promethreusNoticeProperties = promethreusNoticeProperties;
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void run() {
		MicroServiceReport microServiceNotice = serviceNoticeRepository.report();
		if (microServiceNotice.isNeedReport()) {
			int problemCount = microServiceNotice.totalProblemCount();
			logger.debug("prepare for notice: \n " + microServiceNotice);
			ServiceCheckNotice serviceCheckNotice = new ServiceCheckNotice(microServiceNotice,
					promethreusNoticeProperties.getProjectEnviroment(), "服务监控通知");
			serviceCheckNotice.setProblemServiceCount(problemCount);
			noticeSendComponents.forEach(x -> x.send(serviceCheckNotice));
		}
	}

}
