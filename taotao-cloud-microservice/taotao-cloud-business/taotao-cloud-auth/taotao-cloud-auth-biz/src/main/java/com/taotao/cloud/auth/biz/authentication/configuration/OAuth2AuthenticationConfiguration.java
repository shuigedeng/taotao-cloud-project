/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.configuration;

import com.taotao.cloud.auth.biz.authentication.login.form.OAuth2FormLoginUrlConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.social.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.processor.AESCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.RSACryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 认证基础模块配置 </p>
 *
 * @date : 2023/5/13 15:40
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OAuth2AuthenticationProperties.class})
public class OAuth2AuthenticationConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.info("[Herodotus] |- SDK [OAuth2 Authentication] Auto Configure.");
	}

	@Autowired
	private RedisRepository redisRepository;

	@Bean
	DelegateClientRegistrationRepository delegateClientRegistrationRepository(OAuth2ClientProperties properties) {
		DelegateClientRegistrationRepository clientRegistrationRepository = new DelegateClientRegistrationRepository();
		if (properties != null) {
			Map<String, ClientRegistration> clientRegistrations = new OAuth2ClientPropertiesMapper(properties).asClientRegistrations();
			List<ClientRegistration> registrations = new ArrayList<>(clientRegistrations.values());
			registrations.forEach(clientRegistrationRepository::addClientRegistration);
		}
		return clientRegistrationRepository;
	}

	@Bean
	public HttpCryptoProcessor httpCryptoProcessor() {
		return new HttpCryptoProcessor(redisRepository, new RSACryptoProcessor(), new AESCryptoProcessor());
	}

	@Bean
	public LockedUserDetailsStampManager lockedUserDetailsStampManager(OAuth2AuthenticationProperties authenticationProperties) {
		LockedUserDetailsStampManager manager = new LockedUserDetailsStampManager(redisRepository, authenticationProperties);
		log.info("[Herodotus] |- Bean [Locked UserDetails Stamp Manager] Auto Configure.");
		return manager;
	}

	@Bean
	public SignInFailureLimitedStampManager signInFailureLimitedStampManager(OAuth2AuthenticationProperties authenticationProperties) {
		SignInFailureLimitedStampManager manager = new SignInFailureLimitedStampManager(redisRepository, authenticationProperties);
		log.info("[Herodotus] |- Bean [SignIn Failure Limited Stamp Manager] Auto Configure.");
		return manager;
	}

	@Bean
	public OAuth2FormLoginUrlConfigurer auth2FormLoginParameterConfigurer(OAuth2AuthenticationProperties authenticationProperties) {
		OAuth2FormLoginUrlConfigurer configurer = new OAuth2FormLoginUrlConfigurer(authenticationProperties);
		log.info("[Herodotus] |- Bean [OAuth2 FormLogin Parameter Configurer] Auto Configure.");
		return configurer;
	}
}
