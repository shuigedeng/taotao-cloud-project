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

package com.taotao.cloud.auth.biz.management.service;

import com.taotao.cloud.security.springsecurity.core.definition.domain.RegisteredClientDetails;
import com.taotao.cloud.security.springsecurity.core.utils.OAuth2AuthorizationUtils;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * <p>Description: 转换为 RegisteredClient 转换器定义</p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/21 20:36
 */
public interface RegisteredClientConverter<S extends RegisteredClientDetails> extends Converter<S, RegisteredClient> {

    Set<String> getScopes(S details);

    ClientSettings getClientSettings(S details);

    TokenSettings getTokenSettings(S details);

    @Override
    default RegisteredClient convert(S details) {
        Set<String> clientScopes = getScopes(details);
        ClientSettings clientSettings = getClientSettings(details);
        TokenSettings tokenSettings = getTokenSettings(details);

        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(details.getClientAuthenticationMethods());
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
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}
