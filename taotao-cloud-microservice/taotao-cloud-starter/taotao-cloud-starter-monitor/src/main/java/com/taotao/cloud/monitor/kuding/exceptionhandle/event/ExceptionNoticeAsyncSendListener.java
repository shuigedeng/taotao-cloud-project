package com.taotao.cloud.monitor.kuding.exceptionhandle.event;

import java.util.List;
import java.util.concurrent.Executor;

import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeFrequencyStrategyProperties;
import com.taotao.cloud.monitor.kuding.exceptionhandle.statistics.ExceptionNoticeStatisticsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionNoticeAsyncSendListener extends AbstractNoticeSendListener {

	private static final Log logger = LogFactory.getLog(ExceptionNoticeAsyncSendListener.class);

	private final Executor executor;

	/**
	 * @param exceptionNoticeFrequencyStrategyProperties
	 * @param exceptionNoticeStatisticsRepository
	 * @param noticeSendComponents
	 */
	public ExceptionNoticeAsyncSendListener(
			ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties,
                                            ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
                                            List<INoticeSendComponent<ExceptionNotice>> noticeSendComponents, Executor executor) {
		super(exceptionNoticeFrequencyStrategyProperties, exceptionNoticeStatisticsRepository, noticeSendComponents);
		this.executor = executor;
	}

	@Override
	public void onApplicationEvent(ExceptionNoticeEvent event) {
		logger.debug("异步发送消息");
		executor.execute(() -> send(event.getExceptionNotice()));
	}

}
