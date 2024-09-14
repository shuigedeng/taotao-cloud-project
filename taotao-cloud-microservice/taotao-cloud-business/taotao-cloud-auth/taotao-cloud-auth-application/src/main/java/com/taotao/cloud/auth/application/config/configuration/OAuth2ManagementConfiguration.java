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

import com.taotao.cloud.auth.application.service.OAuth2ComplianceService;
import com.taotao.cloud.auth.application.service.OAuth2DeviceService;
import com.taotao.cloud.auth.infrastructure.authentication.response.OAuth2DeviceVerificationResponseHandler;
import com.taotao.cloud.auth.infrastructure.authentication.response.OidcClientRegistrationResponseHandler;
import com.taotao.cloud.auth.infrastructure.compliance.listener.AuthenticationSuccessListener;
import com.taotao.cloud.auth.infrastructure.crypto.AESCryptoProcessor;
import com.taotao.cloud.auth.infrastructure.crypto.HttpCryptoProcessor;
import com.taotao.cloud.auth.infrastructure.stamp.SignInFailureLimitedStampManager;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>OAuth2 Manager 模块配置 </p>
 * <p>
 * {@link org.springframework.security.oauth2.jwt.JwtTimestampValidator}
 *
 * @since : 2022/2/26 12:35
 */
@Configuration
@Import({OAuth2DataJpaConfiguration.class, OAuth2AuthenticationConfiguration.class,
	OAuth2ComplianceConfiguration.class
})
public class OAuth2ManagementConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ManagementConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.info("Module [OAuth2 Management] Auto Configure.");
	}

	/**
	 * redis存储库
	 */
	@Autowired
	private RedisRepository redisRepository;
	/**
	 * http加密处理器
	 *
	 * @return {@link HttpCryptoProcessor }
	 * @since 2023-07-10 17:14:19
	 */
	@Bean
	public HttpCryptoProcessor httpCryptoProcessor() {
		return new HttpCryptoProcessor(redisRepository, new RSACryptoProcessor(),
			new AESCryptoProcessor());
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessListener authenticationSuccessListener(
		SignInFailureLimitedStampManager stampManager,
		OAuth2ComplianceService complianceService,
		OAuth2DeviceService deviceService) {
		AuthenticationSuccessListener listener = new AuthenticationSuccessListener(stampManager,
			complianceService);
		log.info("Bean [OAuth2 Authentication Success Listener] Auto Configure.");
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2DeviceVerificationResponseHandler oauth2DeviceVerificationResponseHandler(
		OAuth2DeviceService oauth2DeviceService) {
		OAuth2DeviceVerificationResponseHandler handler =
			new OAuth2DeviceVerificationResponseHandler(oauth2DeviceService);
		log.info("Bean [OAuth2 Device Verification Response Handler] Auto Configure.");
		return handler;
	}

	@Bean
	@ConditionalOnMissingBean
	public OidcClientRegistrationResponseHandler oidcClientRegistrationResponseHandler(
		OAuth2DeviceService oauth2DeviceService) {
		OidcClientRegistrationResponseHandler handler = new OidcClientRegistrationResponseHandler(
			oauth2DeviceService);
		log.info("Bean [Oidc Client Registration Response Handler] Auto Configure.");
		return handler;
	}
}
