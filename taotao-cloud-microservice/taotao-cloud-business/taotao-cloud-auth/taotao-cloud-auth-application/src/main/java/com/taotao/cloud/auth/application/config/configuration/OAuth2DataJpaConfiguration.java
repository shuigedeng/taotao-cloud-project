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

package com.taotao.cloud.auth.application.config.configuration;

import com.taotao.cloud.auth.application.service.impl.TtcAuthorizationConsentService;
import com.taotao.cloud.auth.application.service.impl.TtcAuthorizationService;
import com.taotao.cloud.auth.application.service.impl.TtcRegisteredClientService;
import com.taotao.cloud.auth.infrastructure.authentication.service.JpaOAuth2AuthorizationConsentService;
import com.taotao.cloud.auth.infrastructure.authentication.service.JpaOAuth2AuthorizationService;
import com.taotao.cloud.auth.infrastructure.authentication.service.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>OAuth2 Manager 模块配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:14:04
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2DataJpaConfiguration {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(OAuth2DataJpaConfiguration.class);

	/**
	 * 后期构建
	 *
	 * @since 2023-07-10 17:14:04
	 */
	@PostConstruct
	public void postConstruct() {
		log.info("SDK [OAuth2 Data JPA] Auto Configure.");
	}

	/**
	 * 注册客户端存储库
	 *
	 * @param ttcRegisteredClientService 希罗多德注册客户服务
	 * @param passwordEncoder            密码编码器
	 * @return {@link RegisteredClientRepository }
	 * @since 2023-07-10 17:14:04
	 */
	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(
		TtcRegisteredClientService ttcRegisteredClientService,
		PasswordEncoder passwordEncoder) {
		JpaRegisteredClientRepository jpaRegisteredClientRepository =
			new JpaRegisteredClientRepository(ttcRegisteredClientService, passwordEncoder);
		log.info("Bean [Jpa Registered Client Repository] Auto Configure.");
		return jpaRegisteredClientRepository;
	}

	/**
	 * 授权服务
	 *
	 * @param ttcAuthorizationService    希罗多德授权服务
	 * @param registeredClientRepository 注册客户端存储库
	 * @return {@link OAuth2AuthorizationService }
	 * @since 2023-07-10 17:14:05
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService(
		TtcAuthorizationService ttcAuthorizationService,
		RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
			new JpaOAuth2AuthorizationService(ttcAuthorizationService,
				registeredClientRepository);
		log.info("Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
		return jpaOAuth2AuthorizationService;
	}

	/**
	 * 授权同意服务
	 *
	 * @param ttcAuthorizationConsentService 希罗多德授权同意服务
	 * @param registeredClientRepository     注册客户端存储库
	 * @return {@link OAuth2AuthorizationConsentService }
	 * @since 2023-07-10 17:14:05
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService(
		TtcAuthorizationConsentService ttcAuthorizationConsentService,
		RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService =
			new JpaOAuth2AuthorizationConsentService(
				ttcAuthorizationConsentService, registeredClientRepository);
		log.info("Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
		return jpaOAuth2AuthorizationConsentService;
	}
}
