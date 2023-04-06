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

package com.taotao.cloud.auth.biz.demo.authorization.configuration;

import cn.herodotus.engine.assistant.core.definition.BearerTokenResolver;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusAuthorizationManager;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusSecurityMetadataSource;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusTokenStrategyConfigurer;
import cn.herodotus.engine.oauth2.authorization.listener.RemoteSecurityMetadataSyncListener;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMatcherConfigurer;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMetadataSourceAnalyzer;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMetadataSourceParser;
import cn.herodotus.engine.oauth2.authorization.storage.SecurityMetadataSourceStorage;
import cn.herodotus.engine.oauth2.core.properties.SecurityProperties;
import cn.herodotus.engine.web.core.properties.EndpointProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * Description: SecurityMetadata 配置
 *
 * @author : gengwei.zheng
 * @date : 2022/1/23 15:42
 */
@AutoConfiguration
@EnableConfigurationProperties({SecurityProperties.class})
@EnableMethodSecurity(proxyTargetClass = true)
public class OAuth2AuthorizationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- SDK [OAuth2 Authorization] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityMetadataSourceStorage securityMetadataSourceStorage() {
        SecurityMetadataSourceStorage securityMetadataSourceStorage = new SecurityMetadataSourceStorage();
        log.trace("[Herodotus] |- Bean [Security Metadata Source Storage] Auto Configure.");
        return securityMetadataSourceStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityMatcherConfigurer securityMatcherConfigurer(SecurityProperties securityProperties) {
        SecurityMatcherConfigurer securityMatcherConfigurer = new SecurityMatcherConfigurer(securityProperties);
        log.trace("[Herodotus] |- Bean [Security Metadata Configurer] Auto Configure.");
        return securityMatcherConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean
    public HerodotusSecurityMetadataSource herodotusSecurityMetadataSource(
            SecurityMetadataSourceStorage securityMetadataSourceStorage,
            SecurityMatcherConfigurer securityMatcherConfigurer) {
        HerodotusSecurityMetadataSource herodotusAuthorizationManager =
                new HerodotusSecurityMetadataSource(securityMetadataSourceStorage, securityMatcherConfigurer);
        log.trace("[Herodotus] |- Bean [Security Metadata Source] Auto Configure.");
        return herodotusAuthorizationManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public HerodotusAuthorizationManager herodotusAuthorizationManager(
            HerodotusSecurityMetadataSource herodotusSecurityMetadataSource) {
        HerodotusAuthorizationManager herodotusAuthorizationManager =
                new HerodotusAuthorizationManager(herodotusSecurityMetadataSource);
        log.trace("[Herodotus] |- Bean [Authorization Manager] Auto Configure.");
        return herodotusAuthorizationManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityMetadataSourceParser securityMetadataSourceParser(
            SecurityMatcherConfigurer securityMatcherConfigurer) {
        SecurityMetadataSourceParser securityMetadataSourceAnalyzer =
                new SecurityMetadataSourceParser(securityMatcherConfigurer);
        log.trace("[Herodotus] |- Bean [Security Metadata Source Parser] Auto Configure.");
        return securityMetadataSourceAnalyzer;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer(
            SecurityMetadataSourceStorage securityMetadataSourceStorage,
            SecurityMetadataSourceParser securityMetadataSourceParser) {
        SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer =
                new SecurityMetadataSourceAnalyzer(securityMetadataSourceStorage, securityMetadataSourceParser);
        log.trace("[Herodotus] |- Bean [Security Metadata Source Analyzer] Auto Configure.");
        return securityMetadataSourceAnalyzer;
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteSecurityMetadataSyncListener remoteSecurityMetadataSyncListener(
            SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer, ServiceMatcher serviceMatcher) {
        RemoteSecurityMetadataSyncListener remoteSecurityMetadataSyncListener =
                new RemoteSecurityMetadataSyncListener(securityMetadataSourceAnalyzer, serviceMatcher);
        log.trace("[Herodotus] |- Bean [Security Metadata Refresh Listener] Auto Configure.");
        return remoteSecurityMetadataSyncListener;
    }

    @Bean
    @ConditionalOnMissingBean
    public HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer(
            JwtDecoder jwtDecoder,
            SecurityProperties securityProperties,
            EndpointProperties endpointProperties,
            OAuth2ResourceServerProperties resourceServerProperties) {
        HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer = new HerodotusTokenStrategyConfigurer(
                jwtDecoder, securityProperties, endpointProperties, resourceServerProperties);
        log.trace("[Herodotus] |- Bean [Token Strategy Configurer] Auto Configure.");
        return herodotusTokenStrategyConfigurer;
    }

    @Bean
    @ConditionalOnBean(HerodotusTokenStrategyConfigurer.class)
    public BearerTokenResolver bearerTokenResolver(HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer) {
        BearerTokenResolver bearerTokenResolver = herodotusTokenStrategyConfigurer.createBearerTokenResolver();
        log.trace("[Herodotus] |- Bean [Bearer Token Resolver] Auto Configure.");
        return bearerTokenResolver;
    }
}
