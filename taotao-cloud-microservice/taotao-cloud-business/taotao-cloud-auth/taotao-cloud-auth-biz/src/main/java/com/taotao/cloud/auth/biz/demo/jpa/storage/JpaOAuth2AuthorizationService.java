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

import cn.herodotus.engine.assistant.core.definition.constants.SymbolConstants;
import cn.herodotus.engine.oauth2.core.jackson2.HerodotusJackson2Module;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusAuthorization;
import cn.herodotus.engine.oauth2.data.jpa.jackson2.OAuth2TokenJackson2Module;
import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusAuthorizationService;
import cn.herodotus.engine.oauth2.data.jpa.utils.OAuth2AuthorizationUtils;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: 基于 JPA 的 OAuth2 认证服务
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 22:16
 */
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);

    private final HerodotusAuthorizationService herodotusAuthorizationService;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JpaOAuth2AuthorizationService(
            HerodotusAuthorizationService herodotusAuthorizationService,
            RegisteredClientRepository registeredClientRepository) {
        this.herodotusAuthorizationService = herodotusAuthorizationService;
        this.registeredClientRepository = registeredClientRepository;

        ClassLoader classLoader = JpaOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        this.objectMapper.registerModules(new HerodotusJackson2Module());
        this.objectMapper.registerModules(new OAuth2TokenJackson2Module());
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        this.herodotusAuthorizationService.saveOrUpdate(toEntity(authorization));
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service save entity.");
    }

    @Transactional
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.herodotusAuthorizationService.deleteById(authorization.getId());
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service remove entity.");
        // TODO： 后期还是考虑改为异步任务的形式，先临时放在这里。
        this.herodotusAuthorizationService.clearHistoryToken();
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service clear history token.");
    }

    @Override
    public OAuth2Authorization findById(String id) {
        HerodotusAuthorization herodotusAuthorization =
                this.herodotusAuthorizationService.findById(id);
        if (ObjectUtils.isNotEmpty(herodotusAuthorization)) {
            log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findById.");
            return toObject(herodotusAuthorization);
        } else {
            return null;
        }
    }

    public int findAuthorizationCount(String registeredClientId, String principalName) {
        int count =
                this.herodotusAuthorizationService.findAuthorizationCount(
                        registeredClientId, principalName);
        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findAuthorizationCount.");
        return count;
    }

    public List<OAuth2Authorization> findAvailableAuthorizations(
            String registeredClientId, String principalName) {
        List<HerodotusAuthorization> authorizations =
                this.herodotusAuthorizationService.findAvailableAuthorizations(
                        registeredClientId, principalName);
        if (CollectionUtils.isNotEmpty(authorizations)) {
            return authorizations.stream().map(this::toObject).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Optional<HerodotusAuthorization> result;
        if (tokenType == null) {
            result =
                    this.herodotusAuthorizationService
                            .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
                                    token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = this.herodotusAuthorizationService.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = this.herodotusAuthorizationService.findByAuthorizationCode(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = this.herodotusAuthorizationService.findByAccessToken(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = this.herodotusAuthorizationService.findByRefreshToken(token);
        } else {
            result = Optional.empty();
        }

        log.debug("[Herodotus] |- Jpa OAuth2 Authorization Service findByToken.");
        return result.map(this::toObject).orElse(null);
    }

    private OAuth2Authorization toObject(HerodotusAuthorization entity) {
        RegisteredClient registeredClient =
                this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '"
                            + entity.getRegisteredClientId()
                            + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder =
                OAuth2Authorization.withRegisteredClient(registeredClient)
                        .id(entity.getId())
                        .principalName(entity.getPrincipalName())
                        .authorizationGrantType(
                                OAuth2AuthorizationUtils.resolveAuthorizationGrantType(
                                        entity.getAuthorizationGrantType()))
                        .attributes(
                                attributes -> attributes.putAll(parseMap(entity.getAttributes())));
        if (entity.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        if (entity.getAuthorizationCode() != null) {
            OAuth2AuthorizationCode authorizationCode =
                    new OAuth2AuthorizationCode(
                            entity.getAuthorizationCode(),
                            DateUtil.toInstant(entity.getAuthorizationCodeIssuedAt()),
                            DateUtil.toInstant(entity.getAuthorizationCodeExpiresAt()));
            builder.token(
                    authorizationCode,
                    metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
        }

        if (entity.getAccessToken() != null) {
            OAuth2AccessToken accessToken =
                    new OAuth2AccessToken(
                            OAuth2AccessToken.TokenType.BEARER,
                            entity.getAccessToken(),
                            DateUtil.toInstant(entity.getAccessTokenIssuedAt()),
                            DateUtil.toInstant(entity.getAccessTokenExpiresAt()),
                            StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            builder.token(
                    accessToken,
                    metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
        }

        if (entity.getRefreshToken() != null) {
            OAuth2RefreshToken refreshToken =
                    new OAuth2RefreshToken(
                            entity.getRefreshToken(),
                            DateUtil.toInstant(entity.getRefreshTokenIssuedAt()),
                            DateUtil.toInstant(entity.getRefreshTokenExpiresAt()));
            builder.token(
                    refreshToken,
                    metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
        }

        if (entity.getOidcIdToken() != null) {
            OidcIdToken idToken =
                    new OidcIdToken(
                            entity.getOidcIdToken(),
                            DateUtil.toInstant(entity.getOidcIdTokenIssuedAt()),
                            DateUtil.toInstant(entity.getOidcIdTokenExpiresAt()),
                            parseMap(entity.getOidcIdTokenClaims()));
            builder.token(
                    idToken,
                    metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
        }

        return builder.build();
    }

    private HerodotusAuthorization toEntity(OAuth2Authorization authorization) {
        HerodotusAuthorization entity = new HerodotusAuthorization();
        entity.setId(authorization.getId());
        entity.setRegisteredClientId(authorization.getRegisteredClientId());
        entity.setPrincipalName(authorization.getPrincipalName());
        entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        entity.setAttributes(writeMap(authorization.getAttributes()));
        entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(
                authorizationCode,
                entity::setAuthorizationCode,
                entity::setAuthorizationCodeIssuedAt,
                entity::setAuthorizationCodeExpiresAt,
                entity::setAuthorizationCodeMetadata);

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                entity::setAccessToken,
                entity::setAccessTokenIssuedAt,
                entity::setAccessTokenExpiresAt,
                entity::setAccessTokenMetadata);
        if (accessToken != null && accessToken.getToken().getScopes() != null) {
            entity.setAccessTokenScopes(
                    StringUtils.collectionToDelimitedString(
                            accessToken.getToken().getScopes(), SymbolConstants.COMMA));
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                entity::setRefreshToken,
                entity::setRefreshTokenIssuedAt,
                entity::setRefreshTokenExpiresAt,
                entity::setRefreshTokenMetadata);

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
                authorization.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                entity::setOidcIdToken,
                entity::setOidcIdTokenIssuedAt,
                entity::setOidcIdTokenExpiresAt,
                entity::setOidcIdTokenMetadata);
        if (oidcIdToken != null) {
            entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
        }

        return entity;
    }

    private void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> tokenValueConsumer,
            Consumer<LocalDateTime> issuedAtConsumer,
            Consumer<LocalDateTime> expiresAtConsumer,
            Consumer<String> metadataConsumer) {
        if (token != null) {
            OAuth2Token oAuth2Token = token.getToken();
            tokenValueConsumer.accept(oAuth2Token.getTokenValue());
            issuedAtConsumer.accept(DateUtil.toLocalDateTime(oAuth2Token.getIssuedAt()));
            expiresAtConsumer.accept(DateUtil.toLocalDateTime(oAuth2Token.getExpiresAt()));
            metadataConsumer.accept(writeMap(token.getMetadata()));
        }
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
