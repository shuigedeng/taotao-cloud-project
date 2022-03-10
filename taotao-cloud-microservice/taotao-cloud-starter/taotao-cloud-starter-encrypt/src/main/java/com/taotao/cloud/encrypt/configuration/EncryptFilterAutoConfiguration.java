package com.taotao.cloud.encrypt.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.encrypt.filter.EncryptFilter;
import com.taotao.cloud.encrypt.handler.EncryptHandler;
import com.taotao.cloud.encrypt.properties.EncryptFilterProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * web配置
 */
@Configuration
@ConditionalOnProperty(prefix = EncryptFilterProperties.PREFIX, name = "enabled", havingValue = "true")
public class EncryptFilterAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(EncryptFilterAutoConfiguration.class, StarterName.ENCRYPT_STARTER);
	}

	private final EncryptHandler encryptHandler;

	private final Environment environment;

	public EncryptFilterAutoConfiguration(EncryptHandler encryptHandler,
		Environment environment) {
		this.encryptHandler = encryptHandler;
		this.environment = environment;
	}

	@Bean
	@Conditional(EncryptDebugCondition.class)
	public FilterRegistrationBean filterRegistrationBean() {
		Integer order = environment.getProperty("encrypt.order", Integer.class);
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new EncryptFilter(encryptHandler));
		bean.addUrlPatterns("/*");
		bean.setName("encryptFilter");
		bean.setOrder(order == null ? 0 : order);
		return bean;
	}

	public static class EncryptDebugCondition implements Condition {

		@Override
		public boolean matches(ConditionContext conditionContext,
			AnnotatedTypeMetadata annotatedTypeMetadata) {
			Environment environment = conditionContext.getEnvironment();
			Boolean debug = environment.getProperty("encrypt.debug", Boolean.class);
			return debug == null || !debug;
		}
	}
}
