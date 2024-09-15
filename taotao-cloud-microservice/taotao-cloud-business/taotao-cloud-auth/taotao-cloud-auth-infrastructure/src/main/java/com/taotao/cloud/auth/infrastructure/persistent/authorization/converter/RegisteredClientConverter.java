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

import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.RegisteredClientDetails;
import com.taotao.boot.security.spring.utils.OAuth2AuthorizationUtils;
import java.util.Set;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

/**
 * <p>转换为 RegisteredClient 转换器定义</p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:32
 */
public interface RegisteredClientConverter<S extends RegisteredClientDetails> extends Converter<S, RegisteredClient> {

    /**
     * 获取范围
     *
     * @param details 详细信息
     * @return {@link Set }<{@link String }>
     * @since 2023-07-10 17:13:33
     */
    Set<String> getScopes(S details);

    /**
     * 获取客户端设置
     *
     * @param details 详细信息
     * @return {@link ClientSettings }
     * @since 2023-07-10 17:13:33
     */
    ClientSettings getClientSettings(S details);

    /**
     * 获取令牌设置
     *
     * @param details 详细信息
     * @return {@link TokenSettings }
     * @since 2023-07-10 17:13:33
     */
    TokenSettings getTokenSettings(S details);

    /**
     * 转换
     *
     * @param details 详细信息
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:13:33
     */
    @Override
    default RegisteredClient convert(S details) {
        Set<String> clientScopes = getScopes(details);
        ClientSettings clientSettings = getClientSettings(details);

        // 客服端设置 设置用户需要确认授权
        // ClientSettings build = ClientSettings.builder().requireAuthorizationConsent(false).build();

        TokenSettings tokenSettings = getTokenSettings(details);

        Set<String> clientAuthenticationMethods =
                StringUtils.commaDelimitedListToSet(details.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(details.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(details.getRedirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(details.getPostLogoutRedirectUris());

        return RegisteredClient.withId(details.getId())
                .clientId(details.getClientId())
                .clientIdIssuedAt(DateUtil.toInstant(details.getClientIdIssuedAt()))
                .clientSecret(details.getClientSecret())
                .clientSecretExpiresAt(DateUtil.toInstant(details.getClientSecretExpiresAt()))
                .clientName(details.getId())
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod -> authenticationMethods.add(
                                OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes.forEach(
                        grantType -> grantTypes.add(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}
