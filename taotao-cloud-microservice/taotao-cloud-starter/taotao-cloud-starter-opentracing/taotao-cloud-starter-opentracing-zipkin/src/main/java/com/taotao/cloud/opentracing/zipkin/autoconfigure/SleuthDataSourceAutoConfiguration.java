package com.taotao.cloud.opentracing.zipkin.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Sleuth DataSource 自动装配
 */
@AutoConfiguration
public class SleuthDataSourceAutoConfiguration implements InitializingBean, EnvironmentAware {

	private Environment environment;


	@Override
	public void afterPropertiesSet() throws Exception {
		String jdbcUrl = environment.getProperty("spring.datasource.url");
		jdbcUrl = jdbcUrl + "&queryInterceptors=brave.mysql8.TracingQueryInterceptor&exceptionInterceptors=brave.mysql8.TracingExceptionInterceptor";
		//todo
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
