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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.configuration;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.processor.AccessHandlerStrategyFactory;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.configuration.JustAuthConfiguration;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.configuration.WxappConfiguration;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.configuration.WxmppConfiguration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>Access 业务模块配置 </p>
 *
 * @since : 2022/1/26 14:59
 */
@Configuration(proxyBeanMethods = false)
@Import({JustAuthConfiguration.class, WxappConfiguration.class, WxmppConfiguration.class})
public class AccessAllConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AccessAllConfiguration.class);

	@PostConstruct
	public void init() {
		log.debug("SDK [Access All] Auto Configure.");
	}

	//	@Configuration(proxyBeanMethods = false)
	//	@ConditionalOnSmsEnabled
	//	@Import({SmsConfiguration.class})
	//	static class PhoneNumberSignInConfiguration {
	//
	//		@Bean(AccountType.PHONE_NUMBER_HANDLER)
	//		@ConditionalOnBean({VerificationCodeStampManager.class, SmsSendStrategyFactory.class})
	//		public PhoneNumberAccessHandler phoneNumberAccessHandler(VerificationCodeStampManager
	// verificationCodeStampManager, SmsSendStrategyFactory smsSendStrategyFactory) {
	//			PhoneNumberAccessHandler phoneNumberAuthenticationHandler = new
	// PhoneNumberAccessHandler(verificationCodeStampManager, smsSendStrategyFactory);
	//			log.trace("Bean [Phone Number SignIn Handler] Auto Configure.");
	//			return phoneNumberAuthenticationHandler;
	//		}
	//	}

	@Bean
	@ConditionalOnMissingBean(AccessHandlerStrategyFactory.class)
	public AccessHandlerStrategyFactory accessHandlerStrategyFactory() {
		AccessHandlerStrategyFactory accessHandlerStrategyFactory = new AccessHandlerStrategyFactory();
		log.trace("Bean [Access Handler Strategy Factory] Auto Configure.");
		return accessHandlerStrategyFactory;
	}

	@Configuration(proxyBeanMethods = false)
	static class ControllerConfiguration {

		@PostConstruct
		public void init() {
			log.debug("SDK [Access All Controller] Auto Configure.");
		}

		//		@Bean
		//		@ConditionalOnSmsEnabled
		//		@ConditionalOnMissingBean
		//		public PhoneNumberAccessController phoneNumberAccessController() {
		//			PhoneNumberAccessController phoneNumberAuthenticationController = new PhoneNumberAccessController();
		//			log.trace("Bean [Phone Number Access Controller] Auto Configure.");
		//			return phoneNumberAuthenticationController;
		//		}

		//		@Bean
		//		@ConditionalOnJustAuthEnabled
		//		@ConditionalOnMissingBean
		//		public JustAuthAccessController justAuthSignInController() {
		//			JustAuthAccessController justAuthAuthenticationController = new JustAuthAccessController();
		//			log.trace("Bean [Just Auth Access Controller] Auto Configure.");
		//			return justAuthAuthenticationController;
		//		}

		//		@Bean
		//		@ConditionalOnWxappEnabled
		//		@ConditionalOnMissingBean
		//		public WxappAccessController wxappAccessController() {
		//			WxappAccessController wxappAccessController = new WxappAccessController();
		//			log.trace("Bean [Wxapp Access Controller] Auto Configure.");
		//			return wxappAccessController;
		//		}
	}
}
