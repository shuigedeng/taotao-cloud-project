package com.taotao.cloud.prometheus.config.exceptionnotice;

import com.taotao.cloud.prometheus.config.annos.ConditionalOnExceptionNotice;
import com.taotao.cloud.prometheus.exceptionhandle.components.InMemeryExceptionStatisticsRepository;
import com.taotao.cloud.prometheus.exceptionhandle.event.AbstractNoticeSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.event.ExceptionNoticeAsyncSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.event.ExceptionNoticeSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.interfaces.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.pojos.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.exception.ExceptionNoticeFrequencyStrategy;
import java.util.List;

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
@EnableConfigurationProperties({ ExceptionNoticeFrequencyStrategy.class })
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
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository) {
		logger.debug("创建同步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeSendListener(exceptionNoticeFrequencyStrategy,
				exceptionNoticeStatisticsRepository, list);
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "prometheus.exceptionnotice.enable-async", havingValue = "true")
	public AbstractNoticeSendListener ExceptionNoticeAsyncSendListener(
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			AsyncTaskExecutor applicationTaskExecutor) {
		logger.debug("创建异步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeAsyncSendListener(exceptionNoticeFrequencyStrategy,
				exceptionNoticeStatisticsRepository, list, applicationTaskExecutor);
		return listener;
	}
}
