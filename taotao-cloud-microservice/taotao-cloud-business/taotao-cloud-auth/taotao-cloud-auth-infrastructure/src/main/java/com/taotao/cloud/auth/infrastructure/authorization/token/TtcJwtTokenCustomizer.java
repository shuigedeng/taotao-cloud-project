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

package com.taotao.cloud.auth.infrastructure.authorization.token;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>自定义 TokenCustomizer </p>
 * <p>
 * 用于自定义的 Ttc User Details 解析。如果使用 Security 默认的 <code>org.springframework.security.core.userdetails.User</code> 则不需要使用该类
 * <p>
 * An OAuth2TokenCustomizer<JwtEncodingContext> declared with a generic type of JwtEncodingContext (implements OAuth2TokenContext) provides the ability to customize the headers and claims of a Jwt.
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:35
 */
public class TtcJwtTokenCustomizer extends AbstractTokenCustomizer
        implements OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * 自定义
     *
     * @param context 上下文
     * @since 2023-07-10 17:25:35
     */
    @Override
    public void customize(JwtEncodingContext context) {
        AbstractAuthenticationToken token = null;
        Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (clientAuthentication instanceof OAuth2ClientAuthenticationToken) {
            token = (OAuth2ClientAuthenticationToken) clientAuthentication;
        }

        if (ObjectUtils.isNotEmpty(token) && token.isAuthenticated()) {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication authentication = context.getPrincipal();
                if (ObjectUtils.isNotEmpty(authentication)) {
                    Map<String, Object> attributes = new HashMap<>();
                    appendAll(attributes, authentication, context.getAuthorizedScopes());
                    JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();
                    jwtClaimSetBuilder.claims(claims -> claims.putAll(attributes));
                }
            }

            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                Authentication authentication = context.getPrincipal();
                if (ObjectUtils.isNotEmpty(authentication)) {
                    Map<String, Object> attributes = new HashMap<>();
                    appendCommons(attributes, authentication, context.getAuthorizedScopes());
                    JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();
                    jwtClaimSetBuilder.claims(claims -> claims.putAll(attributes));
                }
            }
        }
    }
}
