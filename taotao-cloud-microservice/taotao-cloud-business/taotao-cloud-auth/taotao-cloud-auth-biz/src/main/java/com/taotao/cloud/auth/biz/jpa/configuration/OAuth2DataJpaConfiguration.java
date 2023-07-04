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

package com.taotao.cloud.auth.biz.jpa.configuration;

import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationConsentService;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusAuthorizationService;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusRegisteredClientService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaOAuth2AuthorizationConsentService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaOAuth2AuthorizationService;
import com.taotao.cloud.auth.biz.jpa.storage.JpaRegisteredClientRepository;
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
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 *
 * @date : 2022/3/1 18:25
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2DataJpaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2DataJpaConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("SDK [OAuth2 Data JPA] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository(
            HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
        JpaRegisteredClientRepository jpaRegisteredClientRepository =
                new JpaRegisteredClientRepository(herodotusRegisteredClientService, passwordEncoder);
        log.info("Bean [Jpa Registered Client Repository] Auto Configure.");
        return jpaRegisteredClientRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService(
            HerodotusAuthorizationService herodotusAuthorizationService,
            RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
                new JpaOAuth2AuthorizationService(herodotusAuthorizationService, registeredClientRepository);
        log.info("Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
        return jpaOAuth2AuthorizationService;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationConsentService authorizationConsentService(
            HerodotusAuthorizationConsentService herodotusAuthorizationConsentService,
            RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService =
                new JpaOAuth2AuthorizationConsentService(
                        herodotusAuthorizationConsentService, registeredClientRepository);
        log.info("Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
        return jpaOAuth2AuthorizationConsentService;
    }
}
