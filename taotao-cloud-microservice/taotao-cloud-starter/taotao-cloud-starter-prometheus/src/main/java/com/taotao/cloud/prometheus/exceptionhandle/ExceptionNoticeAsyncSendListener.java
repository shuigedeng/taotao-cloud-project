package com.taotao.cloud.prometheus.exceptionhandle;

import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.ExceptionNoticeStrategyProperties;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeAsyncSendListener extends AbstractNoticeSendListener {

	private static final Log logger = LogFactory.getLog(ExceptionNoticeAsyncSendListener.class);

	private final Executor executor;

	/**
	 * @param exceptionNoticeStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeAsyncSendListener(
		ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents, Executor executor) {
		super(exceptionNoticeStrategyProperties, exceptionNoticeStatisticsRepository, noticeSendComponents);
		this.executor = executor;
	}

	@Override
	public void onApplicationEvent(ExceptionNoticeEvent event) {
		logger.debug("异步发送消息");
		executor.execute(() -> send(event.getExceptionNotice()));
	}

}
