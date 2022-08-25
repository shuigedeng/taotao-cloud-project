package com.taotao.cloud.monitor.kuding.exceptionhandle.event;

import java.util.List;

import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeFrequencyStrategy;
import com.taotao.cloud.monitor.kuding.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeSendListener extends AbstractNoticeSendListener {

	private final static Log logger = LogFactory.getLog(ExceptionNoticeSendListener.class);

	/**
	 * @param exceptionNoticeFrequencyStrategy
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeSendListener(ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy,
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
