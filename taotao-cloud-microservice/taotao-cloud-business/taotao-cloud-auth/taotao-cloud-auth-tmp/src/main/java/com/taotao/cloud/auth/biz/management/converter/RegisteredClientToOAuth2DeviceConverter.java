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

package com.taotao.cloud.auth.biz.management.converter;

import com.taotao.cloud.auth.biz.management.entity.OAuth2Device;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.service.OAuth2ScopeService;
import com.taotao.cloud.security.springsecurity.core.enums.Signature;
import com.taotao.cloud.security.springsecurity.core.enums.TokenFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: OAuth2Device 转 RegisteredClient 转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/21 22:05
 */
public class RegisteredClientToOAuth2DeviceConverter implements Converter<RegisteredClient, OAuth2Device> {

    private final OAuth2ScopeService scopeService;

    public RegisteredClientToOAuth2DeviceConverter(OAuth2ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Override
    public OAuth2Device convert(RegisteredClient registeredClient) {

        OAuth2Device device = new OAuth2Device();
        device.setDeviceId(registeredClient.getId());
        device.setDeviceName(registeredClient.getClientName());
        device.setProductId("");
        device.setScopes(getOAuth2Scopes(registeredClient.getScopes()));
        device.setClientId(registeredClient.getClientId());
        device.setClientSecret(registeredClient.getClientSecret());
        device.setClientIdIssuedAt(DateUtil.toLocalDateTime(registeredClient.getClientIdIssuedAt()));
        device.setClientSecretExpiresAt(DateUtil.toLocalDateTime(registeredClient.getClientSecretExpiresAt()));
        device.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(registeredClient.getClientAuthenticationMethods()));
        device.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet())));
        device.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        device.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));

        ClientSettings clientSettings = registeredClient.getClientSettings();
        device.setRequireProofKey(clientSettings.isRequireProofKey());
        device.setRequireAuthorizationConsent(clientSettings.isRequireAuthorizationConsent());
        device.setJwkSetUrl(clientSettings.getJwkSetUrl());
        if (ObjectUtils.isNotEmpty(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm())) {
            device.setAuthenticationSigningAlgorithm(Signature.valueOf(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm().getName()));
        }

        TokenSettings tokenSettings = registeredClient.getTokenSettings();
        device.setAuthorizationCodeValidity(tokenSettings.getAuthorizationCodeTimeToLive());
        device.setAccessTokenValidity(tokenSettings.getAccessTokenTimeToLive());
        device.setDeviceCodeValidity(tokenSettings.getDeviceCodeTimeToLive());
        device.setRefreshTokenValidity(tokenSettings.getRefreshTokenTimeToLive());
        device.setAccessTokenFormat(TokenFormat.get(tokenSettings.getAccessTokenFormat().getValue()));
        device.setReuseRefreshTokens(tokenSettings.isReuseRefreshTokens());
        device.setIdTokenSignatureAlgorithm(Signature.valueOf(tokenSettings.getIdTokenSignatureAlgorithm().getName()));

        return device;
    }

    private Set<OAuth2Scope> getOAuth2Scopes(Set<String> scopes) {
        if (CollectionUtils.isNotEmpty(scopes)) {
            List<String> scopeCodes = new ArrayList<>(scopes);
            List<OAuth2Scope> result = scopeService.findByScopeCodeIn(scopeCodes);
            if (CollectionUtils.isNotEmpty(result)) {
                return new HashSet<>(result);
            }
        }
        return new HashSet<>();
    }
}
