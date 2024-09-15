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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.converter;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcAuthorization;
import com.taotao.boot.security.spring.utils.OAuth2AuthorizationUtils;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * <p>TtcAuthorization 转 OAuth2Authorization 转换器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:53
 */
public class TtcToOAuth2AuthorizationConverter
        extends AbstractOAuth2EntityConverter<TtcAuthorization, OAuth2Authorization> {

    /**
     * 注册客户端存储库
     */
    private final RegisteredClientRepository registeredClientRepository;

    /**
     * 希罗多德到oauth2授权转换器
     *
     * @param jacksonProcessor           杰克逊处理器
     * @param registeredClientRepository 注册客户端存储库
     * @return
     * @since 2023-07-10 17:13:54
     */
    public TtcToOAuth2AuthorizationConverter(
            OAuth2JacksonProcessor jacksonProcessor, RegisteredClientRepository registeredClientRepository) {
        super(jacksonProcessor);
        this.registeredClientRepository = registeredClientRepository;
    }

    /**
     * 转换
     *
     * @param entity 实体
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:13:54
     */
    @Override
    public OAuth2Authorization convert(TtcAuthorization entity) {
        RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException("The RegisteredClient with id '" + entity.getRegisteredClientId()
                    + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(
                        OAuth2AuthorizationUtils.resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
                .attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));
        if (entity.getState() != null) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        if (entity.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    DateUtil.toInstant(entity.getAuthorizationCodeIssuedAt()),
                    DateUtil.toInstant(entity.getAuthorizationCodeExpiresAt()));
            builder.token(
                    authorizationCode, metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
        }

        if (entity.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    entity.getAccessTokenValue(),
                    DateUtil.toInstant(entity.getAccessTokenIssuedAt()),
                    DateUtil.toInstant(entity.getAccessTokenExpiresAt()),
                    StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
        }

        if (entity.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    DateUtil.toInstant(entity.getRefreshTokenIssuedAt()),
                    DateUtil.toInstant(entity.getRefreshTokenExpiresAt()));
            builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
        }

        if (entity.getOidcIdTokenValue() != null) {
            OidcIdToken idToken = new OidcIdToken(
                    entity.getOidcIdTokenValue(),
                    DateUtil.toInstant(entity.getOidcIdTokenIssuedAt()),
                    DateUtil.toInstant(entity.getOidcIdTokenExpiresAt()),
                    parseMap(entity.getOidcIdTokenClaims()));
            builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
        }

        if (entity.getUserCodeValue() != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    entity.getUserCodeValue(),
                    DateUtil.toInstant(entity.getUserCodeIssuedAt()),
                    DateUtil.toInstant(entity.getUserCodeExpiresAt()));
            builder.token(userCode, metadata -> metadata.putAll(parseMap(entity.getUserCodeMetadata())));
        }

        if (entity.getDeviceCodeValue() != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    entity.getDeviceCodeValue(),
                    DateUtil.toInstant(entity.getDeviceCodeIssuedAt()),
                    DateUtil.toInstant(entity.getDeviceCodeExpiresAt()));
            builder.token(deviceCode, metadata -> metadata.putAll(parseMap(entity.getDeviceCodeMetadata())));
        }

        return builder.build();
    }
}
