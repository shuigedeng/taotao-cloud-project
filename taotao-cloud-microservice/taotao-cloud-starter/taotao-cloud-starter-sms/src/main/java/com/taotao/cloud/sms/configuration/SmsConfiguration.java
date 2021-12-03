/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.configuration;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sms.properties.AliSmsProperties;
import com.taotao.cloud.sms.properties.SmsProperties;
import com.taotao.cloud.sms.service.SmsService;
import com.taotao.cloud.sms.service.impl.AliSmsServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SmsConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:45:25
 */
@Configuration
@EnableConfigurationProperties({SmsProperties.class, AliSmsProperties.class})
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", havingValue = "true")
public class SmsConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(SmsConfiguration.class, StarterName.SMS_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "type", havingValue = "ALIYUN")
	public IAcsClient iAcsClient(AliSmsProperties aliSmsProperties) {
		DefaultProfile profile = DefaultProfile.getProfile(
			aliSmsProperties.getRegionId(),
			aliSmsProperties.getAccessKey(),
			aliSmsProperties.getSecretKey());

		DefaultProfile.addEndpoint(
			aliSmsProperties.getRegionId(),
			aliSmsProperties.getProduct(),
			aliSmsProperties.getDomain());

		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		return new DefaultAcsClient(profile);
	}

	@Bean
	@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "type", havingValue = "ALIYUN")
	public SmsService aliSmsTemplate(AliSmsProperties smsProperties, IAcsClient iAcsClient) {
		return new AliSmsServiceImpl(smsProperties, iAcsClient);
	}
}
