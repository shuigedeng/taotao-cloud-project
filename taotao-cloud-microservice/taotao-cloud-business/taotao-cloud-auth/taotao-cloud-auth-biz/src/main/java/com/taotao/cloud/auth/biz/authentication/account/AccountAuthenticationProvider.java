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

package com.taotao.cloud.auth.biz.authentication.account;

import com.taotao.cloud.auth.biz.authentication.account.service.AccountUserDetailsService;
import java.util.Collection;
import org.springframework.beans.factory.InitializingBean;
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

/** 用户+密码登录 */
public class AccountAuthenticationProvider
        implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final AccountUserDetailsService accountUserDetailsService;
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public AccountAuthenticationProvider(AccountUserDetailsService accountUserDetailsService) {
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(
                AccountAuthenticationToken.class,
                authentication,
                () ->
                        messages.getMessage(
                                "AccountVerificationAuthenticationProvider.onlySupports",
                                "Only AccountVerificationAuthenticationProvider is supported"));

        AccountAuthenticationToken unAuthenticationToken =
                (AccountAuthenticationToken) authentication;

        String username = unAuthenticationToken.getName();
        String passowrd = (String) unAuthenticationToken.getCredentials();
        String type = unAuthenticationToken.getType();

        // 验证码校验
        UserDetails userDetails =
                accountUserDetailsService.loadUserByUsername(username, passowrd, type);
        return createSuccessAuthentication(authentication, userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccountAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(accountUserDetailsService, "accountUserDetailsService must not be null");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
     * 认证成功将非授信凭据转为授信凭据. 封装用户信息 角色信息。
     *
     * @param authentication the authentication
     * @param userDetails the user
     * @return the authentication
     */
    protected Authentication createSuccessAuthentication(
            Authentication authentication, UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities =
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities());

        String type = "";
        if (authentication instanceof AccountAuthenticationToken accountAuthenticationToken) {
            type = accountAuthenticationToken.getType();
        }

        AccountAuthenticationToken authenticationToken =
                new AccountAuthenticationToken(userDetails, null, type, authorities);
        authenticationToken.setDetails(authentication.getDetails());

        return authenticationToken;
    }
}
