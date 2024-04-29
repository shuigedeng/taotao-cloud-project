/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.configuration;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.enums.AccountType;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.processor.WxappAccessHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.processor.WxappLogHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.processor.WxappProcessor;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.properties.WxappProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>微信小程序后配置 </p>
 *
 * @since : 2021/3/29 9:27
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WxappProperties.class)
public class WxappConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WxappConfiguration.class);

	@PostConstruct
	public void init() {
		log.debug("SDK [Access Wxapp] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public WxappProcessor wxappProcessor(WxappProperties wxappProperties) {
		WxappProcessor wxappProcessor = new WxappProcessor();
		wxappProcessor.setWxappProperties(wxappProperties);
		wxappProcessor.setWxappLogHandler(new WxappLogHandler());
		log.trace("Bean [Wxapp Processor] Auto Configure.");
		return wxappProcessor;
	}

	@Bean(AccountType.WECHAT_MINI_APP_HANDLER)
	@ConditionalOnBean(WxappProcessor.class)
	@ConditionalOnMissingBean
	public WxappAccessHandler wxappAccessHandler(WxappProcessor wxappProcessor) {
		WxappAccessHandler wxappAccessHandler = new WxappAccessHandler(wxappProcessor);
		log.debug("Bean [Wxapp Access Handler] Auto Configure.");
		return wxappAccessHandler;
	}
}
