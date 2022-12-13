package com.taotao.cloud.tracing.micrometer.autoconfigure;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
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
		LogUtils.started(SleuthDataSourceAutoConfiguration.class,
			StarterName.TRACING_MICROMETER_STARTER);

		String jdbcUrl = environment.getProperty("spring.datasource.url");
		jdbcUrl = jdbcUrl
			+ "&queryInterceptors=brave.mysql8.TracingQueryInterceptor&exceptionInterceptors=brave.mysql8.TracingExceptionInterceptor";
		//todo
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
