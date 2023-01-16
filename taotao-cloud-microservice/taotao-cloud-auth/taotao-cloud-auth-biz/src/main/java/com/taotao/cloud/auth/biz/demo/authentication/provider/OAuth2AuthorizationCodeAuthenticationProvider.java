/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package com.taotao.cloud.auth.biz.demo.authentication.provider;

import cn.herodotus.engine.oauth2.authentication.utils.OAuth2AuthenticationProviderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
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

import java.security.Principal;
import java.util.Map;


/**
 * An {@link AuthenticationProvider} implementation for the OAuth 2.0 Authorization Code Grant.
 *
 * @author Joe Grandja
 * @author Daniel Garnier-Moiroux
 * @see OAuth2AuthorizationCodeAuthenticationToken
 * @see OAuth2AccessTokenAuthenticationToken
 * @see OAuth2AuthorizationCodeRequestAuthenticationProvider
 * @see OAuth2AuthorizationService
 * @see OAuth2TokenGenerator
 * @see <a target="_blank" href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1">Section 4.1 Authorization Code Grant</a>
 * @see <a target="_blank" href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3">Section 4.1.3 Access Token Request</a>
 * @since 0.0.1
 */
public final class OAuth2AuthorizationCodeAuthenticationProvider extends AbstractAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationCodeAuthenticationProvider.class);
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private static final OAuth2TokenType AUTHORIZATION_CODE_TOKEN_TYPE =
            new OAuth2TokenType(OAuth2ParameterNames.CODE);

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the provided parameters.
     *
     * @param authorizationService the authorization service
     * @param tokenGenerator       the token generator
     * @since 0.2.3
     */
    public OAuth2AuthorizationCodeAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthorizationCodeAuthenticationToken authorizationCodeAuthentication =
                (OAuth2AuthorizationCodeAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(authorizationCodeAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        OAuth2Authorization authorization = this.authorizationService.findByToken(
                authorizationCodeAuthentication.getCode(), AUTHORIZATION_CODE_TOKEN_TYPE);
        if (authorization == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);

        OAuth2AuthorizationRequest authorizationRequest = authorization.getAttribute(
                OAuth2AuthorizationRequest.class.getName());

        if (!registeredClient.getClientId().equals(authorizationRequest.getClientId())) {
            if (!authorizationCode.isInvalidated()) {
                // Invalidate the authorization code given that a different client is attempting to use it
                authorization = OAuth2AuthenticationProviderUtils.invalidate(authorization, authorizationCode.getToken());
                this.authorizationService.save(authorization);
            }
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (StringUtils.hasText(authorizationRequest.getRedirectUri()) &&
                !authorizationRequest.getRedirectUri().equals(authorizationCodeAuthentication.getRedirectUri())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!authorizationCode.isActive()) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        Authentication usernamePasswordAuthentication = authorization.getAttribute(Principal.class.getName());

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorization(authorization)
                .authorizedScopes(authorization.getAuthorizedScopes())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrant(authorizationCodeAuthentication);
        // @formatter:on

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);

        // ----- Access token -----
        OAuth2AccessToken accessToken = createOAuth2AccessToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI);

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = creatOAuth2RefreshToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, clientPrincipal, registeredClient);

        // ----- ID token -----
        OidcIdToken idToken = createOidcIdToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, authorizationRequest.getScopes());

        authorization = authorizationBuilder.build();

        // Invalidate the authorization code as it can only be used once
        authorization = OAuth2AuthenticationProviderUtils.invalidate(authorization, authorizationCode.getToken());

        this.authorizationService.save(authorization);

        log.debug("[Herodotus] |- Authorization Code returning OAuth2AccessTokenAuthenticationToken.");

        Map<String, Object> additionalParameters = idTokenAdditionalParameters(idToken);

        OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        return createOAuth2AccessTokenAuthenticationToken(usernamePasswordAuthentication, accessTokenAuthenticationToken);
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
