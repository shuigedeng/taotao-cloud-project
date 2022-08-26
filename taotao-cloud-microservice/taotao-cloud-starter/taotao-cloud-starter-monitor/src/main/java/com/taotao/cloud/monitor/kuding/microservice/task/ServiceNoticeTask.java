package com.taotao.cloud.monitor.kuding.microservice.task;

import java.util.List;

import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.microservice.interfaces.ServiceNoticeRepository;
import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.MicroServiceReport;
import com.taotao.cloud.monitor.kuding.pojos.notice.ServiceCheckNotice;
import com.taotao.cloud.monitor.kuding.properties.NoticeProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceNoticeTask implements Runnable {

	private final List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents;

	private final NoticeProperties noticeProperties;

	private final Log logger = LogFactory.getLog(ServiceNoticeTask.class);

	private final ServiceNoticeRepository serviceNoticeRepository;

	/**
	 * @param serviceCheckNoticeRepository
	 * @param noticeSendComponent
	 * @param reportedFilterHandler
	 * @param noticeProperties
	 */
	public ServiceNoticeTask(List<INoticeSendComponent<ServiceCheckNotice>> noticeSendComponents,
			NoticeProperties noticeProperties, ServiceNoticeRepository serviceNoticeRepository) {
		this.noticeSendComponents = noticeSendComponents;
		this.noticeProperties = noticeProperties;
		this.serviceNoticeRepository = serviceNoticeRepository;
	}

	@Override
	public void run() {
		MicroServiceReport microServiceNotice = serviceNoticeRepository.report();
		if (microServiceNotice.isNeedReport()) {
			int problemCount = microServiceNotice.totalProblemCount();
			logger.debug("prepare for notice: \n " + microServiceNotice);
			ServiceCheckNotice serviceCheckNotice = new ServiceCheckNotice(microServiceNotice,
					noticeProperties.getProjectEnviroment(), "服务监控通知");
			serviceCheckNotice.setProblemServiceCount(problemCount);
			noticeSendComponents.forEach(x -> x.send(serviceCheckNotice));
		}
	}

}
