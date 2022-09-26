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
package com.taotao.cloud.sms.common.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sms.common.executor.DefaultSendAsyncThreadPoolExecutor;
import com.taotao.cloud.sms.common.executor.SendAsyncThreadPoolExecutor;
import com.taotao.cloud.sms.common.handler.SendHandler;
import com.taotao.cloud.sms.common.loadbalancer.ILoadBalancer;
import com.taotao.cloud.sms.common.loadbalancer.RandomSmsLoadBalancer;
import com.taotao.cloud.sms.common.loadbalancer.RoundRobinSmsLoadBalancer;
import com.taotao.cloud.sms.common.loadbalancer.SmsSenderLoadBalancer;
import com.taotao.cloud.sms.common.loadbalancer.WeightRandomSmsLoadBalancer;
import com.taotao.cloud.sms.common.loadbalancer.WeightRoundRobinSmsLoadBalancer;
import com.taotao.cloud.sms.common.model.NoticeData;
import com.taotao.cloud.sms.common.properties.SmsAsyncProperties;
import com.taotao.cloud.sms.common.properties.SmsProperties;
import com.taotao.cloud.sms.common.service.NoticeService;
import com.taotao.cloud.sms.common.service.impl.DefaultNoticeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 短信服务配置
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-26 17:50:03
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties({SmsProperties.class, SmsAsyncProperties.class})
public class SmsAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(SmsAutoConfiguration.class, StarterName.SMS_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public NoticeService noticeService(SmsProperties properties,
									   SmsAsyncProperties asyncProperties,
									   ILoadBalancer<SendHandler, NoticeData> smsSenderLoadbalancer,
									   ObjectProvider<SendAsyncThreadPoolExecutor> executorProvider) {
		return new DefaultNoticeService(properties, asyncProperties, smsSenderLoadbalancer,
			executorProvider.getIfUnique());
	}

	/**
	 * 构造发送异步处理线程池
	 *
	 * @param properties 短信异步配置
	 * @return 发送异步处理线程池
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SmsAsyncProperties.PREFIX, name = "enable", havingValue = "true")
	public SendAsyncThreadPoolExecutor sendAsyncThreadPoolExecutor(SmsAsyncProperties properties) {
		return new DefaultSendAsyncThreadPoolExecutor(properties);
	}

	/**
	 * 构造发送者负载均衡器
	 *
	 * @param properties 短信配置
	 * @return 发送者负载均衡器
	 */
	@Bean
	@ConditionalOnMissingBean
	public SmsSenderLoadBalancer smsSenderLoadBalancer(SmsProperties properties) {
		String type = properties.getLoadBalancerType();
		if (type == null) {
			return new RandomSmsLoadBalancer();
		}

		type = type.trim();

		if (RoundRobinSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
			return new RoundRobinSmsLoadBalancer();
		} else if (WeightRandomSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
			return new WeightRandomSmsLoadBalancer();
		} else if (WeightRoundRobinSmsLoadBalancer.TYPE_NAME.equalsIgnoreCase(type)) {
			return new WeightRoundRobinSmsLoadBalancer();
		} else {
			return new RandomSmsLoadBalancer();
		}
	}

	/**
	 * 创建RestTemplate
	 *
	 * @return RestTemplate
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
