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

package com.taotao.cloud.auth.biz.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * OAuth2EndpointUtils
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class OAuth2EndpointUtils {

    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI =
            "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private OAuth2EndpointUtils() {
    }

    public static MultiValueMap<String, String> getParameters( HttpServletRequest request ) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach(
                ( key, values ) -> {
                    for (String value : values) {
                        parameters.add(key, value);
                    }
                });
        return parameters;
    }

    public static boolean matchesPkceTokenRequest( HttpServletRequest request ) {
        return AuthorizationGrantType.AUTHORIZATION_CODE
                .getValue()
                .equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
                && request.getParameter(OAuth2ParameterNames.CODE) != null
                && request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
    }

    public static void throwError( String errorCode, String description, String errorUri ) {
        OAuth2Error error = new OAuth2Error(errorCode, description, errorUri);
        throw new OAuth2AuthenticationException(error);
    }

    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication ) {
        if (Objects.nonNull(authentication)) {
            // if (authentication instanceof PasswordAuthenticationToken passwordAuthentication) {
            //
            // 	OAuth2ClientAuthenticationToken clientPrincipal = null;
            //
            // 	if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
            // 		passwordAuthentication.getClientPrincipal().getClass())) {
            // 		clientPrincipal = (OAuth2ClientAuthenticationToken)
            // passwordAuthentication.getClientPrincipal();
            // 	}
            //
            // 	if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            // 		return clientPrincipal;
            // 	}
            // }
            //
            // if (authentication instanceof MobileAuthenticationToken mobileAuthenticationToken) {
            //
            // 	OAuth2ClientAuthenticationToken clientPrincipal = null;
            //
            // 	if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
            // 		mobileAuthenticationToken.getClientPrincipal().getClass())) {
            // 		clientPrincipal = (OAuth2ClientAuthenticationToken)
            // mobileAuthenticationToken.getClientPrincipal();
            // 	}
            //
            // 	if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            // 		return clientPrincipal;
            // 	}
            // }
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    public static OAuth2RefreshToken generateRefreshToken(
            Duration tokenTimeToLive, Supplier<String> refreshTokenGenerator ) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(tokenTimeToLive);
        return new OAuth2RefreshToken(refreshTokenGenerator.get(), issuedAt, expiresAt);
    }
}
