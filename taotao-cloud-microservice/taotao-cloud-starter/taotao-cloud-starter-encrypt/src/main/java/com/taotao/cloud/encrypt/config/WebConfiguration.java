package com.taotao.cloud.encrypt.config;

import com.taotao.cloud.encrypt.handler.EncryptHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * web配置
 *
 * @author gaoyang
 */
@Configuration
@AllArgsConstructor
public class WebConfiguration {

	@Autowired(required = false)
	private final EncryptHandler encryptHandler;

	Environment environment;


	@Bean
	@Conditional(DefaultCondition.class)
	public FilterRegistrationBean filterRegistrationBean() {
		Integer order = environment.getProperty("encrypt.order", Integer.class);
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new EncryptFilter(encryptHandler));
		bean.addUrlPatterns("/*");
		bean.setName("encryptFilter");
		bean.setOrder(order == null ? 0 : order);
		return bean;
	}

	static class DefaultCondition implements Condition {
		@Override
		public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
			Environment environment = conditionContext.getEnvironment();
			Boolean debug = environment.getProperty("encrypt.debug", Boolean.class);
			return debug == null || !debug;
		}
	}
}
