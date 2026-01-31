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

package com.taotao.cloud.auth.biz.authentication.utils;

import com.taotao.cloud.auth.biz.exception.IllegalParameterExtensionLoginException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * ExtensionLoginUtils
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class ExtensionLoginUtils {

    private ExtensionLoginUtils() {
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

    public static Map<String, Object> getParameters(
            HttpServletRequest request, String... exclusions ) {
        Map<String, Object> parameters = new HashMap<>(getParameters(request).toSingleValueMap());
        for (String exclusion : exclusions) {
            parameters.remove(exclusion);
        }
        return parameters;
    }

    public static void throwError( String errorCode, String parameterName ) {
        throw new IllegalParameterExtensionLoginException(parameterName + "不能为空");
    }

    private static boolean checkRequired(
            MultiValueMap<String, String> parameters, String parameterName, String parameterValue ) {
        return !StringUtils.hasText(parameterValue) || parameters.get(parameterName).size() != 1;
    }

    private static boolean checkOptional(
            MultiValueMap<String, String> parameters, String parameterName, String parameterValue ) {
        return StringUtils.hasText(parameterValue) && parameters.get(parameterName).size() != 1;
    }

    public static String checkParameter(
            MultiValueMap<String, String> parameters,
            String parameterName,
            boolean isRequired,
            String errorCode ) {
        String value = parameters.getFirst(parameterName);
        if (isRequired) {
            if (checkRequired(parameters, parameterName, value)) {
                ExtensionLoginUtils.throwError(errorCode, parameterName);
            }
        } else {
            if (checkOptional(parameters, parameterName, value)) {
                ExtensionLoginUtils.throwError(errorCode, parameterName);
            }
        }

        return value;
    }

    public static String checkRequiredParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode ) {
        return checkParameter(parameters, parameterName, true, errorCode);
    }

    public static String checkRequiredParameter(
            MultiValueMap<String, String> parameters, String parameterName ) {
        return checkRequiredParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }

    public static String checkOptionalParameter(
            MultiValueMap<String, String> parameters, String parameterName, String errorCode ) {
        return checkParameter(parameters, parameterName, false, errorCode);
    }

    public static String checkOptionalParameter(
            MultiValueMap<String, String> parameters, String parameterName ) {
        return checkOptionalParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }
}
