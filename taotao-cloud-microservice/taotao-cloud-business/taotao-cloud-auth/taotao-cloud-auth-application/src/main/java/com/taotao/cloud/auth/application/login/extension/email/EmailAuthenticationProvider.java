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

package com.taotao.cloud.auth.application.login.extension.email;

import com.taotao.cloud.auth.application.login.extension.captcha.CaptchaAuthenticationToken;
import com.taotao.cloud.auth.application.login.extension.email.service.EmailCheckService;
import com.taotao.cloud.auth.application.login.extension.email.service.EmailUserDetailsService;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.Collection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class EmailAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
    private volatile String userNotFoundEncodedPassword;
    private final UserCache userCache = new NullUserCache();
    private PasswordEncoder passwordEncoder;
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private final UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private EmailUserDetailsService emailUserDetailsService;
    private EmailCheckService emailCheckService;

    public EmailAuthenticationProvider(
            EmailUserDetailsService emailUserDetailsService, EmailCheckService emailCheckService) {
        this.emailUserDetailsService = emailUserDetailsService;
        this.emailCheckService = emailCheckService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(emailUserDetailsService, "emailUserDetailsService must not be null");
        Assert.notNull(emailCheckService, "emailCheckCodeService must not be null");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
                UsernamePasswordAuthenticationToken.class,
                authentication,
                () -> messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // 获取用户名
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        boolean cacheWasUsed = true;
        // 根据用户名从缓存中获得UserDetails对象
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null) {
            cacheWasUsed = false;

            try {
                // 如果缓存中没有信息，通过子类 DaoAuthenticationProvider 实现的 retrieveUser 方法，返回一个 UserDetails 对象
                user = retrieveUser(username, (EmailAuthenticationToken) authentication);
            } catch (UsernameNotFoundException notFound) {
                LogUtils.info("User '" + username + "' not found");

                LogUtils.error("Failed to find user '" + username + "'");
                throw new BadCredentialsException("用户不存在");
            }

            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            // 检查该用户对象的各种状态，比如：账户是否未锁定、账户是否启用、账户是否未过期
            preAuthenticationChecks.check(user);
            // 使用子类 DaoAuthenticationProvider 实现的 additionalAuthenticationChecks方法，检查密码是否输入正确
            additionalAuthenticationChecks(user, (EmailAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveUser(username, (EmailAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user, (EmailAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }
        // 检查该用户对象的各种状态，比如：凭证（密码）是否未过期
        postAuthenticationChecks.check(user);

        // 存入缓存
        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        // 会调用子类方法，设置是否已认证为true，并设置权限信息
        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    /**
     * 认证成功将非授信凭据转为授信凭据. 封装用户信息 角色信息。
     *
     * @param authentication the authentication
     * @param userDetails    the user
     * @return the authentication
     */
    protected Authentication createSuccessAuthentication(
            Object principal, Authentication authentication, UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities =
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities());

        String type = "";
        String verificationCode = "";
        if (authentication instanceof CaptchaAuthenticationToken captchaAuthenticationToken) {
            type = captchaAuthenticationToken.getType();
            verificationCode = captchaAuthenticationToken.getVerificationCode();
        }

        CaptchaAuthenticationToken captchaAuthenticationToken =
                new CaptchaAuthenticationToken(principal, null, verificationCode, type, authorities);
        captchaAuthenticationToken.setDetails(authentication.getDetails());

        return captchaAuthenticationToken;
    }

    // 检查密码是否输入正确
    protected void additionalAuthenticationChecks(UserDetails userDetails, EmailAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            LogUtils.info("Authentication failed: no credentials provided");

            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            LogUtils.info("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    // 通过调用 UserDetailsService 的 loadUserByUsername 方法加载用户信息
    protected final UserDetails retrieveUser(String email, EmailAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            UserDetails loadedUser = this.emailUserDetailsService.loadUserByEmail(email);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(EmailAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    public boolean supports(Class<?> authentication) {
        return (EmailAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            // 用户是否被锁定
            if (!user.isAccountNonLocked()) {
                LogUtils.error("Failed to authenticate since user account is locked");
                throw new LockedException("用户已被锁定");
            }
            // 用户启用
            if (!user.isEnabled()) {
                LogUtils.error("Failed to authenticate since user account is disabled");
                throw new DisabledException("用户未启用");
            }
            // 账号是否过期
            if (!user.isAccountNonExpired()) {
                LogUtils.error("Failed to authenticate since user account has expired");
                throw new AccountExpiredException("用户账号已过期");
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            // 用户密码是否过期
            if (!user.isCredentialsNonExpired()) {
                LogUtils.error("Failed to authenticate since user account credentials have expired");
                throw new CredentialsExpiredException("用户账号已过期");
            }
        }
    }
}
