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
package com.taotao.cloud.sms.web;

import com.taotao.cloud.sms.configuration.SmsAutoConfiguration;
import com.taotao.cloud.sms.service.NoticeService;
import com.taotao.cloud.sms.service.VerificationCodeService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信webmvc自动配置
 *
 * @author shuigedeng
 */
@AutoConfiguration(after = SmsAutoConfiguration.class)
@EnableConfigurationProperties(SmsWebmvcProperties.class)
@ConditionalOnProperty(prefix = SmsWebmvcProperties.PREFIX, name = "enable", havingValue = "true")
public class SmsWebmvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(SmsController.class)
	public SmsController smsController(VerificationCodeService verificationCodeService,
		NoticeService noticeService) {
		return new SmsController(verificationCodeService, noticeService);
	}
}
