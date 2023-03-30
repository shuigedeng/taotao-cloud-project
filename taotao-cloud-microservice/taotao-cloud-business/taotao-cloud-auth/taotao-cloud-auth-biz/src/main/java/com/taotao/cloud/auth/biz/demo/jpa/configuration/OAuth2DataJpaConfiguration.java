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

package com.taotao.cloud.auth.biz.demo.jpa.configuration;

import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusAuthorizationConsentService;
import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusAuthorizationService;
import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusRegisteredClientService;
import cn.herodotus.engine.oauth2.data.jpa.storage.JpaOAuth2AuthorizationConsentService;
import cn.herodotus.engine.oauth2.data.jpa.storage.JpaOAuth2AuthorizationService;
import cn.herodotus.engine.oauth2.data.jpa.storage.JpaRegisteredClientRepository;
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
 * Description: OAuth2 Manager 模块配置
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:25
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {"cn.herodotus.engine.oauth2.data.jpa.entity"})
@EnableJpaRepositories(
        basePackages = {
            "cn.herodotus.engine.oauth2.data.jpa.repository",
        })
@ComponentScan(
        basePackages = {
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
    public RegisteredClientRepository registeredClientRepository(
            HerodotusRegisteredClientService herodotusRegisteredClientService,
            PasswordEncoder passwordEncoder) {
        JpaRegisteredClientRepository jpaRegisteredClientRepository =
                new JpaRegisteredClientRepository(
                        herodotusRegisteredClientService, passwordEncoder);
        log.debug("[Herodotus] |- Bean [Jpa Registered Client Repository] Auto Configure.");
        return jpaRegisteredClientRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService(
            HerodotusAuthorizationService herodotusAuthorizationService,
            RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
                new JpaOAuth2AuthorizationService(
                        herodotusAuthorizationService, registeredClientRepository);
        log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
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
        log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
        return jpaOAuth2AuthorizationConsentService;
    }
}
