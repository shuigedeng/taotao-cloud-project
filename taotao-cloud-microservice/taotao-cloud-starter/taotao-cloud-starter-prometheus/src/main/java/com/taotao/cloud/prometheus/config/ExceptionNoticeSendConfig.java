package com.taotao.cloud.prometheus.config;

import com.taotao.cloud.prometheus.annotation.ConditionalOnExceptionNotice;
import com.taotao.cloud.prometheus.exceptionhandle.AbstractNoticeSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.ExceptionNoticeAsyncSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.ExceptionNoticeSendListener;
import com.taotao.cloud.prometheus.exceptionhandle.ExceptionNoticeStatisticsRepository;
import com.taotao.cloud.prometheus.exceptionhandle.InMemeryExceptionStatisticsRepository;
import com.taotao.cloud.prometheus.message.INoticeSendComponent;
import com.taotao.cloud.prometheus.model.ExceptionNotice;
import com.taotao.cloud.prometheus.properties.ExceptionNoticeStrategyProperties;
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
@EnableConfigurationProperties({ ExceptionNoticeStrategyProperties.class })
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
			ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository) {
		logger.debug("创建同步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeSendListener(
			exceptionNoticeStrategyProperties,
				exceptionNoticeStatisticsRepository, list);
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "prometheus.exceptionnotice.enable-async", havingValue = "true")
	public AbstractNoticeSendListener ExceptionNoticeAsyncSendListener(
			ExceptionNoticeStrategyProperties exceptionNoticeStrategyProperties,
			ExceptionNoticeStatisticsRepository exceptionNoticeStatisticsRepository,
			AsyncTaskExecutor applicationTaskExecutor) {
		logger.debug("创建异步发送监听器");
		AbstractNoticeSendListener listener = new ExceptionNoticeAsyncSendListener(
			exceptionNoticeStrategyProperties,
				exceptionNoticeStatisticsRepository, list, applicationTaskExecutor);
		return listener;
	}
}
