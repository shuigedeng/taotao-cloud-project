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

package com.taotao.cloud.auth.biz.authentication.login.social;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * 授权请求参数的请求参数封装工具类,扩展了{@link OAuth2AuthorizationCodeGrantRequestEntityConverter}
 *
 * @author shuigedeng
 * @version 2023.07
 * @see Converter
 * @since 2023-07-10 17:41:19
 */
public class SocialOAuth2ProviderAuthorizationCodeGrantRequestEntityConverter
        implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	/**
	 * 默认转换器
	 */
	private final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> defaultConverter =
            new OAuth2AuthorizationCodeGrantRequestEntityConverter();

	/**
	 * Returns the {@link RequestEntity} used for the Access Token Request.
	 *
	 * @param authorizationCodeGrantRequest the authorization code grant request
	 * @return {@link RequestEntity }<{@link ? }>
	 * @since 2023-07-10 17:41:19
	 */
	@Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {

        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();

        return Arrays.stream(SocialClientProviders.values())
                .filter(clientProvider -> Objects.equals(clientProvider.registrationId(), registrationId))
                .findAny()
                .map(SocialClientProviders::converter)
                .orElse(defaultConverter)
                .convert(authorizationCodeGrantRequest);
    }
}
