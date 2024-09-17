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

package com.taotao.cloud.auth.infrastructure.authentication.extension;

import com.taotao.cloud.auth.infrastructure.authorization.service.JpaOAuth2AuthorizationService;
import com.taotao.cloud.auth.infrastructure.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.infrastructure.utils.OAuth2AuthenticationProviderUtils;
import com.taotao.cloud.auth.infrastructure.utils.OAuth2EndpointUtils;
import com.taotao.boot.security.spring.constants.OAuth2ErrorKeys;
import com.taotao.boot.security.spring.userdetails.EnhanceUserDetailsService;
import com.taotao.boot.security.spring.exception.AccountEndpointLimitedException;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.Assert;

/**
 * <p>抽象的用户认证Provider </p>
 * <p>
 * 提取公共的用户通用基类，方便涉及用户校验的自定义认证模式使用
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:36:54
 */
public abstract class OAuth2AbstractUserDetailsAuthenticationProvider extends
		OAuth2AbstractAuthenticationProvider {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(OAuth2AbstractUserDetailsAuthenticationProvider.class);

    /**
     * 消息
     */
    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    /**
     * 用户详细信息服务
     */
    private final UserDetailsService userDetailsService;
    /**
     * 授权服务
     */
    private final OAuth2AuthorizationService authorizationService;
    /**
     * 身份验证属性
     */
    private final OAuth2AuthenticationProperties authenticationProperties;
    /**
     * 密码编码器
     */
    private PasswordEncoder passwordEncoder;

    /**
     * oauth2抽象用户详细信息身份验证提供程序
     *
     * @param authorizationService     授权服务
     * @param userDetailsService       用户详细信息服务
     * @param authenticationProperties 身份验证属性
     * @return
     * @since 2023-07-10 17:36:54
     */
    public OAuth2AbstractUserDetailsAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            UserDetailsService userDetailsService,
            OAuth2AuthenticationProperties authenticationProperties) {
        this.userDetailsService = userDetailsService;
        this.authorizationService = authorizationService;
        this.authenticationProperties = authenticationProperties;
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    /**
     * 获取用户详细信息服务
     *
     * @return {@link EnhanceUserDetailsService }
     * @since 2023-07-10 17:36:55
     */
    public EnhanceUserDetailsService getUserDetailsService() {
        return (EnhanceUserDetailsService) userDetailsService;
    }

    /**
     * 设置密码编码器
     *
     * @param passwordEncoder 密码编码器
     * @since 2023-07-10 17:36:55
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获取密码编码器
     *
     * @return {@link PasswordEncoder }
     * @since 2023-07-10 17:36:56
     */
    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * 附加身份验证检查
     *
     * @param userDetails          用户详细信息
     * @param additionalParameters 附加参数
     * @since 2023-07-10 17:36:56
     */
    protected abstract void additionalAuthenticationChecks(
            UserDetails userDetails, Map<String, Object> additionalParameters) throws AuthenticationException;

    /**
     * 检索用户
     *
     * @param additionalParameters 附加参数
     * @return {@link UserDetails }
     * @since 2023-07-10 17:36:56
     */
    protected abstract UserDetails retrieveUser(Map<String, Object> additionalParameters)
            throws AuthenticationException;

    /**
     * 验证用户详细信息
     *
     * @param additionalParameters 附加参数
     * @param registeredClientId   注册客户端id
     * @return {@link Authentication }
     * @since 2023-07-10 17:36:57
     */
    private Authentication authenticateUserDetails(Map<String, Object> additionalParameters, String registeredClientId)
            throws AuthenticationException {
        UserDetails user = retrieveUser(additionalParameters);

        if (!user.isAccountNonLocked()) {
            log.debug("Failed to authenticate since user account is locked");
            throw new LockedException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
        }
        if (!user.isEnabled()) {
            log.debug("Failed to authenticate since user account is disabled");
            throw new DisabledException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            log.debug("Failed to authenticate since user account has expired");
            throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
        }

        additionalAuthenticationChecks(user, additionalParameters);

        if (!user.isCredentialsNonExpired()) {
            log.debug("Failed to authenticate since user account credentials have expired");
            throw new CredentialsExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
        }

        if (authenticationProperties.getSignInEndpointLimited().getEnabled()
                && !authenticationProperties.getSignInKickOutLimited().getEnabled()) {
            if (authorizationService instanceof JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService) {
                int count =
                        jpaOAuth2AuthorizationService.findAuthorizationCount(registeredClientId, user.getUsername());
                if (count >= authenticationProperties.getSignInEndpointLimited().getMaximum()) {
                    throw new AccountEndpointLimitedException("Use same endpoint signIn exceed limit");
                }
            }
        }

        if (!authenticationProperties.getSignInEndpointLimited().getEnabled()
                && authenticationProperties.getSignInKickOutLimited().getEnabled()) {
            if (authorizationService instanceof JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService) {
                List<OAuth2Authorization> authorizations = jpaOAuth2AuthorizationService.findAvailableAuthorizations(
                        registeredClientId, user.getUsername());
                if (CollectionUtils.isNotEmpty(authorizations)) {
                    authorizations.forEach(authorization -> {
                        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                                authorization.getToken(OAuth2RefreshToken.class);
                        if (ObjectUtils.isNotEmpty(refreshToken)) {
                            authorization = OAuth2AuthenticationProviderUtils.invalidate(
                                    authorization, refreshToken.getToken());
                        }
                        log.debug(
                                "Sign in user [{}] with token id [{}] will be kicked out.",
                                user.getUsername(),
                                authorization.getId());
                        jpaOAuth2AuthorizationService.save(authorization);
                    });
                }
            }
        }

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    /**
     * 获取用户名密码认证
     *
     * @param additionalParameters 附加参数
     * @param registeredClientId   注册客户端id
     * @return {@link Authentication }
     * @since 2023-07-10 17:36:57
     */
    protected Authentication getUsernamePasswordAuthentication(
            Map<String, Object> additionalParameters, String registeredClientId) throws AuthenticationException {
        Authentication authentication = null;
        try {
            authentication = authenticateUserDetails(additionalParameters, registeredClientId);
        } catch (AccountStatusException ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            String exceptionName = ase.getClass().getSimpleName();
            OAuth2EndpointUtils.throwError(
                    exceptionName, ase.getMessage(), OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        } catch (BadCredentialsException bce) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorKeys.BAD_CREDENTIALS,
                    bce.getMessage(),
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        } catch (UsernameNotFoundException unfe) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorKeys.USERNAME_NOT_FOUND,
                    unfe.getMessage(),
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        return authentication;
    }
}
