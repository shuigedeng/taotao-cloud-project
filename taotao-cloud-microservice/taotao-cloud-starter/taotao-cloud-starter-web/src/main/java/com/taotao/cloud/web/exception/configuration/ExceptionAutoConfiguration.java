package com.taotao.cloud.web.exception.configuration;

import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import com.taotao.cloud.web.exception.properties.ExceptionHandleProperties;
import com.taotao.cloud.web.exception.resolver.BusinessHandlerExceptionAdvice;
import com.taotao.cloud.web.exception.resolver.FeignHandlerExceptionAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration(after = {
	DingTalkExceptionHandlerAutoConfiguration.class,
	LoggerExceptionHandlerAutoConfiguration.class,
	MailExceptionHandlerAutoConfiguration.class})
@ConditionalOnProperty(prefix = ExceptionHandleProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ExceptionHandleProperties.class)
public class ExceptionAutoConfiguration {

	@Bean
	public BusinessHandlerExceptionAdvice businessHandlerExceptionAdvice(
		List<ExceptionHandler> exceptionHandlers) {
		return new BusinessHandlerExceptionAdvice(exceptionHandlers);
	}

	@Bean
	public FeignHandlerExceptionAdvice feignHandlerExceptionAdvice(
		List<ExceptionHandler> exceptionHandlers) {
		return new FeignHandlerExceptionAdvice(exceptionHandlers);
	}

}
