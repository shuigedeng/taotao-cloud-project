/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.jpa.configuration;

import com.taotao.cloud.auth.biz.dante.jpa.service.HerodotusAuthorizationConsentService;
import com.taotao.cloud.auth.biz.dante.jpa.service.HerodotusAuthorizationService;
import com.taotao.cloud.auth.biz.dante.jpa.service.HerodotusRegisteredClientService;
import com.taotao.cloud.auth.biz.dante.jpa.storage.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:25
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
	"cn.herodotus.engine.oauth2.data.jpa.entity"
})
@EnableJpaRepositories(basePackages = {
	"cn.herodotus.engine.oauth2.data.jpa.repository",
})
@ComponentScan(basePackages = {
	"cn.herodotus.engine.oauth2.data.jpa.service",
})
public class OAuth2DataJpaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(OAuth2DataJpaConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [OAuth2 Data JPA] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public RegisteredClientRepository registeredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
		JpaRegisteredClientRepository jpaRegisteredClientRepository = new JpaRegisteredClientRepository(herodotusRegisteredClientService, passwordEncoder);
		log.debug("[Herodotus] |- Bean [Jpa Registered Client Repository] Auto Configure.");
		return jpaRegisteredClientRepository;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationService authorizationService(HerodotusAuthorizationService herodotusAuthorizationService, RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService = new JpaOAuth2AuthorizationService(herodotusAuthorizationService, registeredClientRepository);
		log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
		return jpaOAuth2AuthorizationService;
	}

	@Bean
	@ConditionalOnMissingBean
	public OAuth2AuthorizationConsentService authorizationConsentService(HerodotusAuthorizationConsentService herodotusAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
		JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService = new JpaOAuth2AuthorizationConsentService(herodotusAuthorizationConsentService, registeredClientRepository);
		log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
		return jpaOAuth2AuthorizationConsentService;
	}
}
