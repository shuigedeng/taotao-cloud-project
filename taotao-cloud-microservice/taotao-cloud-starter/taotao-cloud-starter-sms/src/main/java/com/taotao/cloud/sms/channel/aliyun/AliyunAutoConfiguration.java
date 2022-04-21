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
package com.taotao.cloud.sms.channel.aliyun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.sms.configuration.SmsAutoConfiguration;
import com.taotao.cloud.sms.loadbalancer.SmsSenderLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 阿里云发送端点自动配置
 *
 * @author shuigedeng
 */
@Configuration
@ConditionalOnProperty(prefix = AliyunProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(AliyunProperties.class)
@AutoConfigureAfter(SmsAutoConfiguration.class)
public class AliyunAutoConfiguration {

	/**
	 * 构造阿里云发送处理
	 *
	 * @param properties     配置对象
	 * @param objectMapper   objectMapper
	 * @param loadbalancer   负载均衡器
	 * @param eventPublisher spring应用事件发布器
	 * @return 阿里云发送处理
	 */
	@Bean
	@ConditionalOnBean(SmsSenderLoadBalancer.class)
	public AliyunSendHandler aliyunSendHandler(AliyunProperties properties,
		ObjectMapper objectMapper,
		SmsSenderLoadBalancer loadbalancer,
		ApplicationEventPublisher eventPublisher) {

		AliyunSendHandler handler = new AliyunSendHandler(properties, eventPublisher, objectMapper);
		loadbalancer.addTarget(handler, true);
		loadbalancer.setWeight(handler, properties.getWeight());

		return handler;
	}

	public static class AliyunSendHandlerCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			Boolean enable = context.getEnvironment()
				.getProperty(AliyunProperties.PREFIX + ".enable", Boolean.class);
			return enable == null || enable;
		}
	}

}
