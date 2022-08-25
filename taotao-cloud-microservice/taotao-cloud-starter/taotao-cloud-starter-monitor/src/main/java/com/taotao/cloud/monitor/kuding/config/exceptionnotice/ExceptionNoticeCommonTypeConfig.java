package com.taotao.cloud.monitor.kuding.config.exceptionnotice;

import com.taotao.cloud.monitor.kuding.aop.ExceptionNoticeAop;
import com.taotao.cloud.monitor.kuding.config.annos.ConditionalOnExceptionNotice;
import com.taotao.cloud.monitor.kuding.exceptionhandle.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnExceptionNotice
@ConditionalOnProperty(name = "prometheus.exceptionnotice.listen-type", havingValue = "common", matchIfMissing = true)
public class ExceptionNoticeCommonTypeConfig {

	private final Log logger = LogFactory.getLog(ExceptionNoticeCommonTypeConfig.class);

	@Bean
	@ConditionalOnMissingBean
	public ExceptionNoticeAop exceptionNoticeAop(ExceptionHandler exceptionHandler) {
		logger.debug("创建异常监听切面");
		ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionHandler);
		return aop;
	}
}
