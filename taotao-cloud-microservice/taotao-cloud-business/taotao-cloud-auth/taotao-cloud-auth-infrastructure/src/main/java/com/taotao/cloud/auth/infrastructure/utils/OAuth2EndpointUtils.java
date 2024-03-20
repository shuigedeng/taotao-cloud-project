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

package com.taotao.cloud.auth.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * <p>OAuth 2.0 Endpoint 工具方法类 </p>
 * <p>
 * 新版 spring-security-oauth2-authorization-server 很多代码都是“包”级可访问的，外部无法使用。为了方便扩展将其提取出来，便于使用。
 * <p>
 * 代码内容与原包代码基本一致。
 *
 *
 * @since : 2022/2/23 11:24
 */
public class OAuth2EndpointUtils {

    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI =
            "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private OAuth2EndpointUtils() {}

    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    public static Map<String, Object> getParametersIfMatchesAuthorizationCodeGrantRequest(
            HttpServletRequest request, String... exclusions) {
        if (!matchesAuthorizationCodeGrantRequest(request)) {
            return Collections.emptyMap();
        }
        return getParameters(request, exclusions);
    }

    public static Map<String, Object> getParameters(HttpServletRequest request, String... exclusions) {
        Map<String, Object> parameters = new HashMap<>(getParameters(request).toSingleValueMap());
        for (String exclusion : exclusions) {
            parameters.remove(exclusion);
        }
        return parameters;
    }

    public static boolean matchesClientCredentialsGrantRequest(HttpServletRequest request) {
        return AuthorizationGrantType.CLIENT_CREDENTIALS
                .getValue()
                .equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE));
    }

    public static boolean matchesAuthorizationCodeGrantRequest(HttpServletRequest request) {
        return AuthorizationGrantType.AUTHORIZATION_CODE
                        .getValue()
                        .equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
                && request.getParameter(OAuth2ParameterNames.CODE) != null;
    }

    public static boolean matchesPkceTokenRequest(HttpServletRequest request) {
        return matchesAuthorizationCodeGrantRequest(request)
                && request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
    }

    public static void throwError(String errorCode, String parameterName) {
        throwError(errorCode, parameterName, ACCESS_TOKEN_REQUEST_ERROR_URI);
    }

    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthenticationException(error);
    }

    private static boolean checkRequired(
            MultiValueMap<String, String> parameters, String parameterName, String parameterValue) {
        return !StringUtils.hasText(parameterValue)
                || parameters.get(parameterName).size() != 1;
    }

    private static boolean checkOptional(
            MultiValueMap<String, String> parameters, String parameterName, String parameterValue) {
        return StringUtils.hasText(parameterValue)
                && parameters.get(parameterName).size() != 1;
    }

    public static String checkParameter(
            MultiValueMap<String, String> parameters,
            String parameterName,
            boolean isRequired,
            String errorCode,
            String errorUri) {
        String value = parameters.getFirst(parameterName);
        if (isRequired) {
            if (checkRequired(parameters, parameterName, value)) {
                OAuth2EndpointUtils.throwError(errorCode, parameterName, errorUri);
            }
        } else {
            if (checkOptional(parameters, parameterName, value)) {
                OAuth2EndpointUtils.throwError(errorCode, parameterName, errorUri);
            }
        }

        return value;
    }

    public static String checkRequiredParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode, String errorUri) {
        return checkParameter(parameters, parameterName, true, errorCode, errorUri);
    }

    public static String checkRequiredParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode) {
        return checkRequiredParameter(
                parameters, parameterName, errorCode, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }

    public static String checkRequiredParameter(MultiValueMap<String, String> parameters, String parameterName) {
        return checkRequiredParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }

    public static String checkOptionalParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode, String errorUri) {
        return checkParameter(parameters, parameterName, false, errorCode, errorUri);
    }

    public static String checkOptionalParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode) {
        return checkOptionalParameter(
                parameters, parameterName, errorCode, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }

    public static String checkOptionalParameter(MultiValueMap<String, String> parameters, String parameterName) {
        return checkOptionalParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }
}
