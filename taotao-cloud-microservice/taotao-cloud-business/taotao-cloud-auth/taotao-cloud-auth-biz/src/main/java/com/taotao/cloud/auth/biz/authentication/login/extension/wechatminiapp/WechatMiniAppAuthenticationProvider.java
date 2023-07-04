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

package com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp;

import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.client.WechatMiniAppRequest;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.service.WechatMiniAppSessionKeyCacheService;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.service.WechatMiniAppUserDetailsService;
import java.util.Collection;
import java.util.Objects;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/** 微信小程序登录 */
public class WechatMiniAppAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService;
    private WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService;
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public WechatMiniAppAuthenticationProvider(
            WechatMiniAppUserDetailsService wechatMiniAppUserDetailsService,
            WechatMiniAppSessionKeyCacheService wechatMiniAppSessionKeyCacheService) {
        this.wechatMiniAppUserDetailsService = wechatMiniAppUserDetailsService;
        this.wechatMiniAppSessionKeyCacheService = wechatMiniAppSessionKeyCacheService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
                WechatMiniAppAuthenticationToken.class,
                authentication,
                () -> messages.getMessage(
                        "MiniAppAuthenticationProvider.onlySupports", "Only MiniAppAuthenticationToken is supported"));

        WechatMiniAppAuthenticationToken unAuthenticationToken = (WechatMiniAppAuthenticationToken) authentication;
        WechatMiniAppRequest credentials = (WechatMiniAppRequest) unAuthenticationToken.getCredentials();

        String clientId = credentials.getClientId();
        String openId = credentials.getOpenId();

        UserDetails userDetails = wechatMiniAppUserDetailsService.loadByOpenId(clientId, openId);
        if (Objects.isNull(userDetails)) {
            userDetails = wechatMiniAppUserDetailsService.register(
                    credentials, wechatMiniAppSessionKeyCacheService.get(clientId + "::" + openId));
        }
        return createSuccessAuthentication(authentication, userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatMiniAppAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
     * 认证成功将非授信凭据转为授信凭据. 封装用户信息 角色信息。
     *
     * @param authentication the authentication
     * @param user the user
     * @return the authentication
     */
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {

        Collection<? extends GrantedAuthority> authorities = authoritiesMapper.mapAuthorities(user.getAuthorities());
        WechatMiniAppAuthenticationToken authenticationToken = new WechatMiniAppAuthenticationToken(user, authorities);
        authenticationToken.setDetails(authentication.getPrincipal());

        return authenticationToken;
    }
}
