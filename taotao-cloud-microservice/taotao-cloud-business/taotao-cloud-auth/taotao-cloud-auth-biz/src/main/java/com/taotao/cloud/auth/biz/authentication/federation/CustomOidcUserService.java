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

package com.taotao.cloud.auth.biz.authentication.federation;

import com.taotao.cloud.auth.biz.authentication.federation.strategy.context.Oauth2UserConverterContext;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

/**
 * 自定义三方oidc登录用户信息服务
 */
// @Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final IOauth2ThirdAccountService thirdAccountService;

    private final Oauth2UserConverterContext userConverterContext;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 获取三方用户信息
        OidcUser oidcUser = super.loadUser(userRequest);
        // 转为项目中的三方用户信息
        Oauth2ThirdAccount oauth2ThirdAccount = userConverterContext.convert(userRequest, oidcUser);
        // 检查用户信息
        thirdAccountService.checkAndSaveUser(oauth2ThirdAccount);
        OidcIdToken oidcIdToken = oidcUser.getIdToken();
        // 将loginType设置至attributes中
        LinkedHashMap<String, Object> attributes = new LinkedHashMap<>(oidcIdToken.getClaims());
        // 将RegistrationId当做登录类型
        attributes.put("loginType", userRequest.getClientRegistration().getRegistrationId());
        // 重新生成一个idToken
        OidcIdToken idToken =
                new OidcIdToken(
                        oidcIdToken.getTokenValue(),
                        oidcIdToken.getIssuedAt(),
                        oidcIdToken.getExpiresAt(),
                        attributes);
        String userNameAttributeName =
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();
        // 重新生成oidcUser
        if (StringUtils.hasText(userNameAttributeName)) {
            return new DefaultOidcUser(
                    oidcUser.getAuthorities(),
                    idToken,
                    oidcUser.getUserInfo(),
                    userNameAttributeName);
        }
        return new DefaultOidcUser(oidcUser.getAuthorities(), idToken, oidcUser.getUserInfo());
    }
}
