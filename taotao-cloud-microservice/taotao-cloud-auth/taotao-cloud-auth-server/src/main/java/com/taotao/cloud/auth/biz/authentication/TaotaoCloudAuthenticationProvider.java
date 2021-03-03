/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.auth.biz.authentication;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.auth.biz.service.ISmsCodeService;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.security.service.IUserDetailsService;
import com.taotao.cloud.security.token.TaotaoCloudAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TaotaoCloudAuthenticationProvider
 *
 * @author dengtao
 * @since 2020/10/19 11:21
 * @version 1.0.0
 */
public class TaotaoCloudAuthenticationProvider implements
        AuthenticationProvider, InitializingBean, MessageSourceAware {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new TaotaoCloudAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new TaotaoCloudAuthenticationProvider.DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private volatile String userNotFoundEncodedPassword;
    private PasswordEncoder passwordEncoder;
    private IUserDetailsService userDetailService;
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private ISmsCodeService smsCodeService;

    public TaotaoCloudAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        Assert.notNull(this.userDetailService, "A UserDetailsService must be set");
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(TaotaoCloudAuthenticationToken.class, authentication,
                () -> messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only TaotaoCloudAuthenticationToken is supported"));
        TaotaoCloudAuthenticationToken authenticationToken = (TaotaoCloudAuthenticationToken) authentication;
        String grantType = authenticationToken.getGrantType();
        Object principal = authenticationToken.getPrincipal();
        Object credentials = authenticationToken.getCredentials();

        if (Objects.isNull(principal)) {
            throw new BadCredentialsException("参数错误");
        }
        String username = principal.toString();

        // 第一步校验参数
        if (CommonConstant.PHONE_LOGIN.equals(grantType)) {
            if (StrUtil.isBlank(username)) {
                throw new BadCredentialsException("手机号码错误");
            }
            String phoneRegEx = "1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                throw new BadCredentialsException("手机号码格式错误");
            }

            if (Objects.isNull(credentials)) {
                throw new BadCredentialsException("验证码为空");
            }
            String verifyCode = credentials.toString();
            if (StrUtil.isBlank(username)) {
                throw new BadCredentialsException("验证码错误");
            }
            smsCodeService.validate(username, verifyCode);
        } else if (CommonConstant.QR_LOGIN.equals(grantType)) {
            if (StrUtil.isBlank(username)) {
                throw new BadCredentialsException("二维码code错误");
            }
        }

        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null) {
            cacheWasUsed = false;
            try {
                user = retrieveUser(username,
                        (TaotaoCloudAuthenticationToken) authentication);
            } catch (UsernameNotFoundException notFound) {
                LogUtil.error("User '" + username + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
                } else {
                    throw notFound;
                }
            }
            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }
        return doAuthenticationCheck(user, authentication, username, cacheWasUsed);
    }

    // 第二步校验
    protected Authentication doAuthenticationCheck(UserDetails user,
                                                   Authentication authentication,
                                                   String username,
                                                   Boolean cacheWasUsed) {
        try {
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user,
                    (TaotaoCloudAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveUser(username,
                        (TaotaoCloudAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user,
                        (TaotaoCloudAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }
        postAuthenticationChecks.check(user);
        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }
        Object principalToReturn = user;
        if (forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }
        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    // 第三步校验
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  TaotaoCloudAuthenticationToken authentication)
            throws AuthenticationException {
        Object credentials = authentication.getCredentials();
        if (credentials == null) {
            LogUtil.error("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = credentials.toString();
            String grantType = authentication.getGrantType();
            if (CommonConstant.PHONE_LOGIN.equals(grantType)) {
                // 手机验证码验证，调用公共服务查询后台验证码缓存： key 为authentication.getPrincipal()的value， 并判断其与验证码是否匹配,
                // if (!"1000".equals(presentedPassword)) {
                //     this.logger.debug("Authentication failed: verifyCode does not match stored value");
                //     throw new BadCredentialsException(this.messages.getMessage("MyAbstractUserDetailsAuthenticationProvider.badCredentials", "Bad verifyCode"));
                // }
            } else if (CommonConstant.QR_LOGIN.equals(grantType)) {
                // 二维码只需要根据 qrCode 查询到用户即可，所以此处无需验证
            } else {
                // 用户名密码验证
                if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                    LogUtil.error("认证失败密码错误");
                    throw new BadCredentialsException("认证失败密码错误l");
                }
            }
        }
    }

    protected final UserDetails retrieveUser(String username,
                                             TaotaoCloudAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            String userType = authentication.getUserType();
            String grantType = authentication.getGrantType();
            SecurityUser loadedUser = this.getUserDetailService().loadUserSecurityUser(username, userType, grantType);
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

    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication,
                                                         UserDetails user) {
        TaotaoCloudAuthenticationToken result = new TaotaoCloudAuthenticationToken(
                principal,
                authentication.getCredentials(),
                ((TaotaoCloudAuthenticationToken) authentication).getGrantType(),
                ((TaotaoCloudAuthenticationToken) authentication).getUserType(),
                this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(TaotaoCloudAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public boolean isForcePrincipalAsString() {
        return forcePrincipalAsString;
    }

    public boolean isHideUserNotFoundExceptions() {
        return hideUserNotFoundExceptions;
    }

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public ISmsCodeService getSmsCodeService() {
        return smsCodeService;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setSmsCodeService(ISmsCodeService smsCodeService) {
        Assert.notNull(smsCodeService, "smsCodeService cannot be null");
        this.smsCodeService = smsCodeService;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (TaotaoCloudAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return preAuthenticationChecks;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    public IUserDetailsService getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(IUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                LogUtil.error("用户已锁定");
                throw new LockedException("用户已锁定");
            }

            if (!user.isEnabled()) {
                LogUtil.error("用户不可用");
                throw new DisabledException("用户不可用");
            }

            if (!user.isAccountNonExpired()) {
                LogUtil.error("用户账号已过期");
                throw new AccountExpiredException("用户账号已过期");
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                LogUtil.error("用户账号凭据已过期");
                throw new CredentialsExpiredException("用户账号凭据已过期");
            }
        }
    }

}
