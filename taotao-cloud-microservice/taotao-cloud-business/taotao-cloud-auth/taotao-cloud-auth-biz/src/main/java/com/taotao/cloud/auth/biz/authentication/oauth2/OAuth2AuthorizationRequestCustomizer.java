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

package com.taotao.cloud.auth.biz.authentication.oauth2;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * customizer {@link OAuth2AuthorizationRequest}
 *
 * <p>client_id 变成 appid ，并追加锚点#wechat_redirect
 *
 * @author felord.cn
 * @see DefaultOAuth2AuthorizationRequestResolver#setAuthorizationRequestCustomizer(Consumer)
 */
public class OAuth2AuthorizationRequestCustomizer {

    /**
     * 授权请求参数定制
     *
     * @param builder the builder
     */
    public static void customize(OAuth2AuthorizationRequest.Builder builder) {
        builder.attributes(attributes -> Arrays.stream(ClientProviders.values())
                .filter(clientProvider -> Objects.equals(
                        clientProvider.registrationId(), attributes.get(OAuth2ParameterNames.REGISTRATION_ID)))
                .findAny()
                .map(ClientProviders::requestConsumer)
                .ifPresent(requestConsumer -> requestConsumer.accept(builder)));
    }
}
