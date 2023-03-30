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

package com.taotao.cloud.auth.biz.demo.jpa.storage;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusAuthorizationConsent;
import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusAuthorizationConsentService;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * Description: 基于 JPA 的 OAuth2 认证服务
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 22:15
 */
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final Logger log =
            LoggerFactory.getLogger(JpaOAuth2AuthorizationConsentService.class);

    private final HerodotusAuthorizationConsentService herodotusAuthorizationConsentService;
    private final RegisteredClientRepository registeredClientRepository;

    public JpaOAuth2AuthorizationConsentService(
            HerodotusAuthorizationConsentService herodotusAuthorizationConsentService,
            RegisteredClientRepository registeredClientRepository) {
        this.herodotusAuthorizationConsentService = herodotusAuthorizationConsentService;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Consent Service save entity.");
        this.herodotusAuthorizationConsentService.save(toEntity(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Consent Service remove entity.");
        this.herodotusAuthorizationConsentService.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Consent Service findById.");
        return this.herodotusAuthorizationConsentService
                .findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
                .map(this::toObject)
                .orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(
            HerodotusAuthorizationConsent authorizationConsent) {
        String registeredClientId = authorizationConsent.getRegisteredClientId();
        RegisteredClient registeredClient =
                this.registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '"
                            + registeredClientId
                            + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2AuthorizationConsent.Builder builder =
                OAuth2AuthorizationConsent.withId(
                        registeredClientId, authorizationConsent.getPrincipalName());
        if (authorizationConsent.getAuthorities() != null) {
            for (String authority :
                    StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())) {
                builder.authority(new HerodotusGrantedAuthority(authority));
            }
        }

        return builder.build();
    }

    private HerodotusAuthorizationConsent toEntity(
            OAuth2AuthorizationConsent authorizationConsent) {
        HerodotusAuthorizationConsent entity = new HerodotusAuthorizationConsent();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
