/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.authorization.customizer;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 自定义 TokenCustomizer </p>
 * <p>
 * 用于自定义的 Herodotus User Details 解析。如果使用 Security 默认的 <code>org.springframework.security.core.userdetails.User</code> 则不需要使用该类
 *
 * @author : gengwei.zheng
 * @date : 2022/2/23 22:17
 */
public class HerodotusTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {

        AbstractAuthenticationToken token = null;
        Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (clientAuthentication instanceof OAuth2ClientAuthenticationToken) {
            token = (OAuth2ClientAuthenticationToken) clientAuthentication;
        }

        if (ObjectUtils.isNotEmpty(token)) {
            if (token.isAuthenticated() && OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication authentication = context.getPrincipal();
                if (ObjectUtils.isNotEmpty(authentication)) {
                    if (authentication instanceof UsernamePasswordAuthenticationToken) {
                        HerodotusUser principal = (HerodotusUser) authentication.getPrincipal();
                        String userId = principal.getUserId();
                        Set<String> authorities = principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());
                        Set<String> authorizedScopes = context.getAuthorizedScopes();

                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put(BaseConstants.OPEN_ID, userId);
                        attributes.put(BaseConstants.AUTHORITIES, authorities);
                        if (CollectionUtils.isNotEmpty(authorizedScopes)) {
                            attributes.put(OAuth2ParameterNames.SCOPE, authorizedScopes);
                        }

                        JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();
                        jwtClaimSetBuilder.claims(claims -> claims.putAll(attributes));
                    }

                    if (authentication instanceof OAuth2ClientAuthenticationToken) {
                        OAuth2ClientAuthenticationToken clientAuthenticationToken = (OAuth2ClientAuthenticationToken) authentication;

                        Map<String, Object> attributes = new HashMap<>();
                        if (CollectionUtils.isNotEmpty(clientAuthenticationToken.getAuthorities())) {
                            Set<String> authorities = clientAuthenticationToken.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toSet());
                            attributes.put(BaseConstants.AUTHORITIES, authorities);
                        }

                        Set<String> authorizedScopes = context.getAuthorizedScopes();
                        if (CollectionUtils.isNotEmpty(authorizedScopes)) {
                            attributes.put(OAuth2ParameterNames.SCOPE, authorizedScopes);
                        }

                        JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();
                        jwtClaimSetBuilder.claims(claims -> claims.putAll(attributes));
                    }
                }
            }
        }
    }
}
