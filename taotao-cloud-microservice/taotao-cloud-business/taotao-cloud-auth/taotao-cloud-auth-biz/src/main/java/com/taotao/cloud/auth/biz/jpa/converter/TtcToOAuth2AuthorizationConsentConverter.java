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

import com.taotao.boot.security.spring.core.authority.TtcGrantedAuthority;
import com.taotao.cloud.auth.biz.jpa.entity.TtcAuthorizationConsent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * <p>TtcAuthorizationConsent 转 OAuth2AuthorizationConsent 转换器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:09:35
 */
public class TtcToOAuth2AuthorizationConsentConverter
        implements Converter<TtcAuthorizationConsent, OAuth2AuthorizationConsent> {

    private final RegisteredClientRepository registeredClientRepository;

    public TtcToOAuth2AuthorizationConsentConverter(
            RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public OAuth2AuthorizationConsent convert(TtcAuthorizationConsent authorizationConsent) {
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
                builder.authority(new TtcGrantedAuthority(authority));
            }
        }

        return builder.build();
    }
}
