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

package com.taotao.cloud.auth.biz.demo.server.service;

import cn.herodotus.engine.assistant.core.enums.Target;
import cn.herodotus.engine.assistant.core.exception.transaction.TransactionalRollbackException;
import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.core.properties.SecurityProperties;
import cn.herodotus.engine.oauth2.data.jpa.repository.HerodotusRegisteredClientRepository;
import cn.herodotus.engine.oauth2.data.jpa.utils.OAuth2AuthorizationUtils;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Application;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Scope;
import cn.herodotus.engine.oauth2.server.authentication.repository.OAuth2ApplicationRepository;
import cn.hutool.core.date.DateUtil;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Description: OAuth2ApplicationService
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:06
 */
@Service
public class OAuth2ApplicationService extends BaseLayeredService<OAuth2Application, String> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final HerodotusRegisteredClientRepository herodotusRegisteredClientRepository;
    private final OAuth2ApplicationRepository applicationRepository;
    private final SecurityProperties securityProperties;

    @Autowired
    public OAuth2ApplicationService(
            RegisteredClientRepository registeredClientRepository,
            HerodotusRegisteredClientRepository herodotusRegisteredClientRepository,
            OAuth2ApplicationRepository applicationRepository,
            SecurityProperties securityProperties) {
        this.registeredClientRepository = registeredClientRepository;
        this.herodotusRegisteredClientRepository = herodotusRegisteredClientRepository;
        this.applicationRepository = applicationRepository;
        this.securityProperties = securityProperties;
    }

    @Override
    public BaseRepository<OAuth2Application, String> getRepository() {
        return this.applicationRepository;
    }

    @Transactional(rollbackFor = TransactionalRollbackException.class)
    @Override
    public OAuth2Application saveOrUpdate(OAuth2Application entity) {
        OAuth2Application application = super.saveOrUpdate(entity);
        if (ObjectUtils.isNotEmpty(application)) {
            registeredClientRepository.save(toRegisteredClient(application));
            log.debug("[Herodotus] |- OAuth2ApplicationService saveOrUpdate.");
            return application;
        } else {
            log.error("[Herodotus] |- OAuth2ApplicationService saveOrUpdate error, rollback data!");
            throw new NullPointerException("save or update OAuth2Application failed");
        }
    }

    @Transactional(rollbackFor = TransactionalRollbackException.class)
    @Override
    public void deleteById(String id) {
        super.deleteById(id);
        herodotusRegisteredClientRepository.deleteById(id);
        log.debug("[Herodotus] |- OAuth2ApplicationService deleteById.");
    }

    public OAuth2Application authorize(String applicationId, String[] scopeIds) {

        Set<OAuth2Scope> scopes = new HashSet<>();
        for (String scopeId : scopeIds) {
            OAuth2Scope scope = new OAuth2Scope();
            scope.setScopeId(scopeId);
            scopes.add(scope);
        }

        OAuth2Application oldApplication = findById(applicationId);
        oldApplication.setScopes(scopes);

        OAuth2Application newApplication = saveOrUpdate(oldApplication);
        log.debug("[Herodotus] |- OAuth2ApplicationService assign.");
        return newApplication;
    }

    public OAuth2Application findByClientId(String clientId) {
        OAuth2Application application = applicationRepository.findByClientId(clientId);
        log.debug("[Herodotus] |- OAuth2ApplicationService findByClientId.");
        return application;
    }

    private RegisteredClient toRegisteredClient(OAuth2Application application) {

        Set<String> clientAuthenticationMethods =
                StringUtils.commaDelimitedListToSet(application.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes =
                StringUtils.commaDelimitedListToSet(application.getAuthorizationGrantTypes());
        Set<String> redirectUris =
                StringUtils.commaDelimitedListToSet(application.getRedirectUris());
        Set<OAuth2Scope> clientScopes = application.getScopes();

        return RegisteredClient.withId(application.getApplicationId())
                // 客户端id 需要唯一
                .clientId(application.getClientId())
                // 客户端密码
                .clientSecret(application.getClientSecret())
                .clientSecretExpiresAt(DateUtil.toInstant(application.getClientSecretExpiresAt()))
                .clientAuthenticationMethods(
                        authenticationMethods ->
                                clientAuthenticationMethods.forEach(
                                        authenticationMethod ->
                                                authenticationMethods.add(
                                                        OAuth2AuthorizationUtils
                                                                .resolveClientAuthenticationMethod(
                                                                        authenticationMethod))))
                .authorizationGrantTypes(
                        (grantTypes) ->
                                authorizationGrantTypes.forEach(
                                        grantType ->
                                                grantTypes.add(
                                                        OAuth2AuthorizationUtils
                                                                .resolveAuthorizationGrantType(
                                                                        grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes(
                        (scopes) ->
                                clientScopes.forEach(
                                        clientScope -> scopes.add(clientScope.getScopeCode())))
                .clientSettings(createClientSettings(application))
                .tokenSettings(createTokenSettings(application))
                .build();
    }

    private ClientSettings createClientSettings(OAuth2Application application) {
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder();
        clientSettingsBuilder.requireAuthorizationConsent(
                application.getRequireAuthorizationConsent());
        clientSettingsBuilder.requireProofKey(application.getRequireProofKey());
        if (StringUtils.hasText(application.getJwkSetUrl())) {
            clientSettingsBuilder.jwkSetUrl(application.getJwkSetUrl());
        }
        if (ObjectUtils.isNotEmpty(application.getAuthenticationSigningAlgorithm())) {
            JwsAlgorithm jwsAlgorithm =
                    SignatureAlgorithm.from(application.getAuthenticationSigningAlgorithm().name());
            if (ObjectUtils.isNotEmpty(jwsAlgorithm)) {
                clientSettingsBuilder.tokenEndpointAuthenticationSigningAlgorithm(jwsAlgorithm);
            }
        }
        return clientSettingsBuilder.build();
    }

    private TokenSettings createTokenSettings(OAuth2Application application) {
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
        // accessToken 的有效期
        tokenSettingsBuilder.accessTokenTimeToLive(application.getAccessTokenValidity());
        // refreshToken 的有效期
        tokenSettingsBuilder.refreshTokenTimeToLive(application.getRefreshTokenValidity());
        // 是否可重用刷新令牌
        tokenSettingsBuilder.reuseRefreshTokens(application.getReuseRefreshTokens());
        tokenSettingsBuilder.authorizationCodeTimeToLive(application.getAuthorizationCodeTtl());
        tokenSettingsBuilder.accessTokenFormat(getTokenFormat());
        if (ObjectUtils.isNotEmpty(application.getIdTokenSignatureAlgorithm())) {
            SignatureAlgorithm signatureAlgorithm =
                    SignatureAlgorithm.from(application.getIdTokenSignatureAlgorithm().name());
            if (ObjectUtils.isNotEmpty(signatureAlgorithm)) {
                tokenSettingsBuilder.idTokenSignatureAlgorithm(signatureAlgorithm);
            }
        }
        return tokenSettingsBuilder.build();
    }

    private OAuth2TokenFormat getTokenFormat() {
        if (securityProperties.getValidate() == Target.REMOTE) {
            return new OAuth2TokenFormat("reference");
        } else {
            return new OAuth2TokenFormat("self-contained");
        }
    }
}
