package com.taotao.cloud.monitor.kuding.config.exceptionnotice;

import java.util.List;

import com.taotao.cloud.monitor.kuding.message.INoticeSendComponent;
import com.taotao.cloud.monitor.kuding.properties.exception.ExceptionNoticeFrequencyStrategyProperties;
import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnExceptionNotice;
import com.taotao.cloud.monitor.kuding.exceptionhandle.statistics.InMemeryExceptionStatisticsRepository;
import com.taotao.cloud.monitor.kuding.exceptionhandle.event.AbstractNoticeSendListener;
import com.taotao.cloud.monitor.kuding.exceptionhandle.event.ExceptionNoticeAsyncSendListener;
import com.taotao.cloud.monitor.kuding.exceptionhandle.event.ExceptionNoticeSendListener;
import com.taotao.cloud.monitor.kuding.exceptionhandle.statistics.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;


@Configuration
@ConditionalOnExceptionNotice
@EnableConfigurationProperties({ ExceptionNoticeFrequencyStrategyProperties.class })
public class ExceptionNoticeSendConfig {

	@Autowired
	private List<INoticeSendComponent<ExceptionNotice>> list;

	private final Log logger = LogFactory.getLog(ExceptionNoticeSendConfig.class);

	@Bean
	@ConditionalOnMissingBean
	public ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository() {
		logger.debug("创建默认异常统计仓库");
		ExceptionNoticeStatisticsRepository repository = new InMemeryExceptionStatisticsRepository();
		return repository;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "prometheus.exceptionnotice.enable-async", havingValue = "false", matchIfMissing = true)
	public AbstractNoticeSendListener exceptionNoticeSendListener(
			ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository) {
		logger.debug("创建同步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeSendListener(
			exceptionNoticeFrequencyStrategyProperties,
				exceptionNoticeStatisticsRepository, list);
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "prometheus.exceptionnotice.enable-async", havingValue = "true")
	public AbstractNoticeSendListener ExceptionNoticeAsyncSendListener(
			ExceptionNoticeFrequencyStrategyProperties exceptionNoticeFrequencyStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			AsyncTaskExecutor applicationTaskExecutor) {
		logger.debug("创建异步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeAsyncSendListener(
			exceptionNoticeFrequencyStrategyProperties,
				exceptionNoticeStatisticsRepository, list, applicationTaskExecutor);
		return listener;
	}
}
