package com.taotao.cloud.prometheus.exceptionhandle.event;

import com.taotao.cloud.prometheus.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.pojos.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.exception.ExceptionNoticeFrequencyStrategy;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeSendListener extends AbstractNoticeSendListener {

	private final static Log logger = LogFactory.getLog(ExceptionNoticeSendListener.class);

	/**
	 * @param exceptionNoticeFrequencyStrategy
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeSendListener(
		ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents) {
		super(exceptionNoticeFrequencyStrategy, exceptionNoticeStatisticsRepository, noticeSendComponents);
	}

	@Override
	public void onApplicationEvent(ExceptionNoticeEvent event) {
		logger.debug("消息同步发送");
		send(event.getExceptionNotice());
	}

}
