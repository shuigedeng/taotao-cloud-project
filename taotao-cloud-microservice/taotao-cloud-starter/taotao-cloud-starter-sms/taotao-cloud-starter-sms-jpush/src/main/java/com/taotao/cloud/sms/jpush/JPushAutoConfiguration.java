/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.jpush;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.sms.common.configuration.SmsAutoConfiguration;
import com.taotao.cloud.sms.common.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.client.RestTemplate;

/**
 * 极光发送端点自动配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:06
 */
@AutoConfiguration(after = SmsAutoConfiguration.class)
@ConditionalOnProperty(prefix = JPushProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(JPushProperties.class)
public class JPushAutoConfiguration {

	/**
	 * 构造极光发送处理
	 *
	 * @param properties     配置对象
	 * @param loadbalancer   负载均衡器
	 * @param objectMapper   objectMapper
	 * @param eventPublisher spring应用事件发布器
	 * @param restTemplate   restTemplate
	 * @return 极光发送处理
	 */
	@Bean
	@ConditionalOnBean(SmsSenderLoadBalancer.class)
	public JPushSendHandler jpushSendHandler(JPushProperties properties,
		SmsSenderLoadBalancer loadbalancer,
		ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher,
		RestTemplate restTemplate) {
		JPushSendHandler handler = new JPushSendHandler(properties, eventPublisher, objectMapper,
			restTemplate);
		loadbalancer.addTarget(handler, true);
		loadbalancer.setWeight(handler, properties.getWeight());
		return handler;
	}

	public static class JPushSendHandlerCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			Boolean enable = context.getEnvironment()
				.getProperty(JPushProperties.PREFIX + ".enable", Boolean.class);
			return enable == null || enable;
		}
	}

}
