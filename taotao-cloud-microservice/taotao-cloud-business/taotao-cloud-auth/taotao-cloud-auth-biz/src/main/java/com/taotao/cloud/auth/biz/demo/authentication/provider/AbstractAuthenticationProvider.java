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

package com.taotao.cloud.auth.biz.demo.authentication.provider;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import cn.herodotus.engine.oauth2.core.utils.PrincipalUtils;
import java.util.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

/**
 * Description: 抽象的认证Provider
 *
 * <p>提取公共的通用认证基类，方便设置返回Token的信息设置
 *
 * @author : gengwei.zheng
 * @date : 2022/10/14 12:46
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    protected OAuth2AccessToken createOAuth2AccessToken(
            DefaultOAuth2TokenContext.Builder tokenContextBuilder,
            OAuth2Authorization.Builder authorizationBuilder,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            String errorUri) {
        OAuth2TokenContext tokenContext =
                tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(
                    OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.",
                    errorUri);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(),
                tokenContext.getAuthorizedScopes());

        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(
                    accessToken,
                    (metadata) -> metadata.put(
                            OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        return accessToken;
    }

    protected OAuth2RefreshToken creatOAuth2RefreshToken(
            DefaultOAuth2TokenContext.Builder tokenContextBuilder,
            OAuth2Authorization.Builder authorizationBuilder,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            String errorUri,
            OAuth2ClientAuthenticationToken clientPrincipal,
            RegisteredClient registeredClient) {
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
                &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            OAuth2TokenContext tokenContext =
                    tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);

            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(
                        OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.",
                        errorUri);
                throw new OAuth2AuthenticationException(error);
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }
        return refreshToken;
    }

    protected OidcIdToken createOidcIdToken(
            DefaultOAuth2TokenContext.Builder tokenContextBuilder,
            OAuth2Authorization.Builder authorizationBuilder,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            String errorUri,
            Set<String> requestedScopes) {
        OidcIdToken idToken;
        if (requestedScopes.contains(OidcScopes.OPENID)) {
            OAuth2TokenContext tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build()) // ID token customizer may need access to the
                    // access token and/or refresh token
                    .build();
            OAuth2Token generatedIdToken = tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(
                        OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.",
                        errorUri);
                throw new OAuth2AuthenticationException(error);
            }
            idToken = new OidcIdToken(
                    generatedIdToken.getTokenValue(),
                    generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(),
                    ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(
                    idToken,
                    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }
        return idToken;
    }

    protected Map<String, Object> idTokenAdditionalParameters(OidcIdToken idToken) {
        Map<String, Object> additionalParameters = Collections.emptyMap();
        if (idToken != null) {
            additionalParameters = new HashMap<>();
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }
        return additionalParameters;
    }

    protected Set<String> validateScopes(Set<String> requestedScopes, RegisteredClient registeredClient) {
        Set<String> authorizedScopes = registeredClient.getScopes();
        if (!CollectionUtils.isEmpty(requestedScopes)) {
            for (String requestedScope : requestedScopes) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }
        return authorizedScopes;
    }

    protected OAuth2AccessTokenAuthenticationToken createOAuth2AccessTokenAuthenticationToken(
            Authentication source, OAuth2AccessTokenAuthenticationToken destination) {
        if (source instanceof UsernamePasswordAuthenticationToken) {
            if (source.getPrincipal() instanceof HerodotusUser user) {
                destination.setDetails(PrincipalUtils.toPrincipalDetails(user));
            }
        }

        return destination;
    }
}
