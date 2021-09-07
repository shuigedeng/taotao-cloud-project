package com.taotao.cloud.prometheus.exceptionhandle;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.ExceptionNoticeStrategyProperties;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeSendListener extends AbstractNoticeSendListener {

	private final static Log logger = LogFactory.getLog(ExceptionNoticeSendListener.class);

	/**
	 * @param exceptionNoticeStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeSendListener(
		ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents) {
		super(exceptionNoticeStrategyProperties, exceptionNoticeStatisticsRepository, noticeSendComponents);
	}

	@Override
	public void onApplicationEvent(ExceptionNoticeEvent event) {
		logger.debug("消息同步发送");
		send(event.getExceptionNotice());
	}

}
