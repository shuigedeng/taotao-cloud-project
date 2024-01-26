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

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorizationConsent;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.util.StringUtils;

/**
 * <p>OAuth2AuthorizationConsent 转 HerodotusAuthorizationConsent 转换器</p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:47
 */
public class OAuth2ToHerodotusAuthorizationConsentConverter
        implements Converter<OAuth2AuthorizationConsent, HerodotusAuthorizationConsent> {
    /**
     * 转换
     *
     * @param authorizationConsent 授权同意书
     * @return {@link HerodotusAuthorizationConsent }
     * @since 2023-07-10 17:13:47
     */
    @Override
    public HerodotusAuthorizationConsent convert(OAuth2AuthorizationConsent authorizationConsent) {
        HerodotusAuthorizationConsent entity = new HerodotusAuthorizationConsent();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
