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

package com.taotao.cloud.auth.biz.management.configuration;

import com.taotao.cloud.auth.biz.authentication.configuration.OAuth2AuthenticationConfiguration;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.jpa.configuration.OAuth2DataJpaConfiguration;
import com.taotao.cloud.auth.biz.management.compliance.listener.AuthenticationSuccessListener;
import com.taotao.cloud.auth.biz.management.response.OAuth2DeviceVerificationResponseHandler;
import com.taotao.cloud.auth.biz.management.response.OidcClientRegistrationResponseHandler;
import com.taotao.cloud.auth.biz.management.service.OAuth2ComplianceService;
import com.taotao.cloud.auth.biz.management.service.OAuth2DeviceService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 * <p>
 * {@link org.springframework.security.oauth2.jwt.JwtTimestampValidator}
 *
 *
 * @date : 2022/2/26 12:35
 */
@Configuration
@Import({OAuth2DataJpaConfiguration.class, OAuth2AuthenticationConfiguration.class, OAuth2ComplianceConfiguration.class})
public class OAuth2ManagementConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2ManagementConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.info("[Herodotus] |- Module [OAuth2 Management] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessListener authenticationSuccessListener(SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService, OAuth2DeviceService deviceService) {
		AuthenticationSuccessListener listener = new AuthenticationSuccessListener(stampManager, complianceService);
		log.info("[Herodotus] |- Bean [OAuth2 Authentication Success Listener] Auto Configure.");
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2DeviceVerificationResponseHandler oauth2DeviceVerificationResponseHandler(OAuth2DeviceService oauth2DeviceService) {
		OAuth2DeviceVerificationResponseHandler handler = new OAuth2DeviceVerificationResponseHandler(oauth2DeviceService);
		log.info("[Herodotus] |- Bean [OAuth2 Device Verification Response Handler] Auto Configure.");
		return handler;
	}

	@Bean
	@ConditionalOnMissingBean
	public OidcClientRegistrationResponseHandler oidcClientRegistrationResponseHandler(OAuth2DeviceService oauth2DeviceService) {
		OidcClientRegistrationResponseHandler handler = new OidcClientRegistrationResponseHandler(oauth2DeviceService);
		log.info("[Herodotus] |- Bean [Oidc Client Registration Response Handler] Auto Configure.");
		return handler;
	}

}
