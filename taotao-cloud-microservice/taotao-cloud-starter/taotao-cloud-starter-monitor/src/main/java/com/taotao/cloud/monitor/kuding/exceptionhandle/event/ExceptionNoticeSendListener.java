package com.taotao.cloud.monitor.kuding.exceptionhandle.event;

import com.taotao.cloud.monitor.kuding.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeFrequencyStrategyProperties;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeSendListener extends AbstractNoticeSendListener {

	private final static Log logger = LogFactory.getLog(ExceptionNoticeSendListener.class);

	/**
	 * @param exceptionNoticeFrequencyStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeSendListener(
		ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties,
		ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
		List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents) {
		super(exceptionNoticeFrequencyStrategyProperties, exceptionNoticeStatisticsRepository,
			noticeSendComponents);
	}

	@Override
	public void onApplicationEvent(ExceptionNoticeEvent event) {
		logger.debug("消息同步发送");
		send(event.getExceptionNotice());
	}

}
