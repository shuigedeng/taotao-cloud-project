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

package com.taotao.cloud.auth.biz.jpa.converter;

import com.taotao.cloud.auth.biz.jpa.entity.TtcRegisteredClient;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import java.util.ArrayList;
import java.util.List;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;

/**
 * <p>RegisteredClient 转 TtcRegisteredClient 转换器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:37
 */
public class OAuth2ToTtcRegisteredClientConverter
        extends AbstractOAuth2EntityConverter<RegisteredClient, TtcRegisteredClient> {

    /**
     * 密码编码器
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * oauth2到希罗多德注册客户端转换器
     *
     * @param jacksonProcessor 杰克逊处理器
     * @param passwordEncoder  密码编码器
     * @return
     * @since 2023-07-10 17:13:38
     */
    public OAuth2ToTtcRegisteredClientConverter(
            OAuth2JacksonProcessor jacksonProcessor, PasswordEncoder passwordEncoder) {
        super(jacksonProcessor);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 转换
     *
     * @param registeredClient 注册客户
     * @return {@link TtcRegisteredClient }
     * @since 2023-07-10 17:13:38
     */
    @Override
    public TtcRegisteredClient convert(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods =
                new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient
                .getClientAuthenticationMethods()
                .forEach(
                        clientAuthenticationMethod ->
                                clientAuthenticationMethods.add(
                                        clientAuthenticationMethod.getValue()));

        List<String> authorizationGrantTypes =
                new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient
                .getAuthorizationGrantTypes()
                .forEach(
                        authorizationGrantType ->
                                authorizationGrantTypes.add(authorizationGrantType.getValue()));

        TtcRegisteredClient entity = new TtcRegisteredClient();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(
                DateUtil.toLocalDateTime(registeredClient.getClientIdIssuedAt()));
        entity.setClientSecret(encode(registeredClient.getClientSecret()));
        entity.setClientSecretExpiresAt(
                DateUtil.toLocalDateTime(registeredClient.getClientSecretExpiresAt()));
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(
                StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(
                StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(
                StringUtils.collectionToCommaDelimitedString(
                        registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(
                StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    /**
     * 编码
     *
     * @param value 值
     * @return {@link String }
     * @since 2023-07-10 17:13:39
     */
    private String encode(String value) {
        if (value != null) {
            return this.passwordEncoder.encode(value);
        }
        return null;
    }
}
