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

import com.taotao.cloud.auth.infrastructure.utils.OAuth2AuthenticationProviderUtils;
import java.security.Principal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An {@link AuthenticationProvider} implementation for the OAuth 2.0 Authorization Code Grant.
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OAuth2AuthorizationCodeAuthenticationToken
 * @see OAuth2AccessTokenAuthenticationToken
 * @see OAuth2AuthorizationCodeRequestAuthenticationProvider
 * @see OAuth2AuthorizationService
 * @see OAuth2TokenGenerator
 * @see <a target="_blank" href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1">Section 4.1 Authorization Code Grant</a>
 * @see <a target="_blank" href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3">Section 4.1.3 Access Token Request</a>
 * @since 2023-07-10 17:37:26
 */
public final class OAuth2AuthorizationCodeAuthenticationProvider extends
		OAuth2AbstractAuthenticationProvider {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationCodeAuthenticationProvider.class);

    /**
     * 错误uri
     */
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    /**
     * 授权代码令牌类型
     */
    private static final OAuth2TokenType AUTHORIZATION_CODE_TOKEN_TYPE = new OAuth2TokenType(OAuth2ParameterNames.CODE);

    /**
     * 授权服务
     */
    private final OAuth2AuthorizationService authorizationService;
    /**
     * 令牌生成器
     */
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    /**
     * 会话注册表
     */
    private SessionRegistry sessionRegistry;

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the provided parameters.
     *
     * @param authorizationService the authorization service
     * @param tokenGenerator       the token generator
     * @return
     * @since 2023-07-10 17:39:44
     */
    public OAuth2AuthorizationCodeAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * 验证
     *
     * @param authentication 身份验证
     * @return {@link Authentication }
     * @since 2023-07-10 17:39:44
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthorizationCodeAuthenticationToken authorizationCodeAuthentication =
                (OAuth2AuthorizationCodeAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(
                        authorizationCodeAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        log.trace("Retrieved registered client");

        OAuth2Authorization authorization = this.authorizationService.findByToken(
                authorizationCodeAuthentication.getCode(), AUTHORIZATION_CODE_TOKEN_TYPE);
        if (authorization == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        log.trace("Retrieved authorization with authorization code");

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);

        OAuth2AuthorizationRequest authorizationRequest =
                authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

        if (!registeredClient.getClientId().equals(authorizationRequest.getClientId())) {
            if (!authorizationCode.isInvalidated()) {
                // Invalidate the authorization code given that a different client is attempting to use it
                authorization =
                        OAuth2AuthenticationProviderUtils.invalidate(authorization, authorizationCode.getToken());
                this.authorizationService.save(authorization);

                log.warn("Invalidated authorization code used by registered client '{}'", registeredClient.getId());
            }
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (StringUtils.hasText(authorizationRequest.getRedirectUri())
                && !authorizationRequest.getRedirectUri().equals(authorizationCodeAuthentication.getRedirectUri())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!authorizationCode.isActive()) {
            if (authorizationCode.isInvalidated()) {
                OAuth2Authorization.Token<? extends OAuth2Token> token = authorization.getRefreshToken() != null
                        ? authorization.getRefreshToken()
                        : authorization.getAccessToken();
                if (token != null) {
                    // Invalidate the access (and refresh) token as the client is attempting to use the authorization
                    // code more than once
                    authorization = OAuth2AuthenticationProviderUtils.invalidate(authorization, token.getToken());
                    this.authorizationService.save(authorization);
                    log.warn(
                            "Invalidated authorization token(s) previously issued to registered client '{}'",
                            registeredClient.getId());
                }
            }
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        log.trace("Validated token request parameters");

        Authentication principal = authorization.getAttribute(Principal.class.getName());

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(principal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorization(authorization)
                .authorizedScopes(authorization.getAuthorizedScopes())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrant(authorizationCodeAuthentication);
        // @formatter:on

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);

        // ----- Access token -----
        OAuth2AccessToken accessToken =
                createOAuth2AccessToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI);

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = creatOAuth2RefreshToken(
                tokenContextBuilder,
                authorizationBuilder,
                this.tokenGenerator,
                ERROR_URI,
                clientPrincipal,
                registeredClient);

        // ----- ID token -----
        OidcIdToken idToken = createOidcIdToken(
                principal,
                sessionRegistry,
                tokenContextBuilder,
                authorizationBuilder,
                this.tokenGenerator,
                ERROR_URI,
                authorizationRequest.getScopes());

        authorization = authorizationBuilder.build();

        // Invalidate the authorization code as it can only be used once
        authorization = OAuth2AuthenticationProviderUtils.invalidate(authorization, authorizationCode.getToken());

        this.authorizationService.save(authorization);

        log.debug("Authorization Code returning OAuth2AccessTokenAuthenticationToken.");

        Map<String, Object> additionalParameters = idTokenAdditionalParameters(idToken);

        OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        return createOAuth2AccessTokenAuthenticationToken(principal, accessTokenAuthenticationToken);
    }

    /**
     * Sets the {@link SessionRegistry} used to track OpenID Connect sessions.
     *
     * @param sessionRegistry the
     * @since 2023-07-10 17:39:44
     */
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        Assert.notNull(sessionRegistry, "sessionRegistry cannot be null");
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * 支持
     *
     * @param authentication 身份验证
     * @return boolean
     * @since 2023-07-10 17:39:44
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
