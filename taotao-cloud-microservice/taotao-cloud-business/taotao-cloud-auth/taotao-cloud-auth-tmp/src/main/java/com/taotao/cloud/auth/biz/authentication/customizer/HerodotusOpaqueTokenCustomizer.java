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

package com.taotao.cloud.auth.biz.authentication.customizer;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: Opaque Token Customizer </p>
 * <p>
 * An {@link OAuth2TokenCustomizer} provides the ability to customize the attributes of an OAuth2Token, which are accessible in the provided {@link org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext}.
 * It is used by an {@link org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator} to let it customize the attributes of the OAuth2Token before it is generated.
 *
 * @author : gengwei.zheng
 * @date : 2022/10/9 20:43
 */
public class HerodotusOpaqueTokenCustomizer extends AbstractTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {

        AbstractAuthenticationToken token = null;
        Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (clientAuthentication instanceof OAuth2ClientAuthenticationToken) {
            token = (OAuth2ClientAuthenticationToken) clientAuthentication;
        }

        if (ObjectUtils.isNotEmpty(token)) {
            if (token.isAuthenticated()) {

                if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                    Authentication authentication = context.getPrincipal();
                    if (ObjectUtils.isNotEmpty(authentication)) {
                        Map<String, Object> attributes = new HashMap<>();
                        appendAll(attributes, authentication, context.getAuthorizedScopes());
                        OAuth2TokenClaimsSet.Builder tokenClaimSetBuilder = context.getClaims();
                        tokenClaimSetBuilder.claims(claims -> claims.putAll(attributes));
                    }
                }
            }

        }
    }
}
