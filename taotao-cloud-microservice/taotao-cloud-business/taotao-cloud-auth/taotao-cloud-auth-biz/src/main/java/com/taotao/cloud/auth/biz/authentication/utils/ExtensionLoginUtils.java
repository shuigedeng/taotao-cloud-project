/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
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
 * 4.分发源码时候，请注明软件出处
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.utils;

import com.taotao.cloud.auth.biz.authentication.exception.ExtensionLoginException;
import com.taotao.cloud.auth.biz.authentication.exception.IllegalParameterExtensionLoginException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtensionLoginUtils {

    private ExtensionLoginUtils() {
    }

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

    public static Map<String, Object> getParameters(HttpServletRequest request, String... exclusions) {
        Map<String, Object> parameters = new HashMap<>(getParameters(request).toSingleValueMap());
        for (String exclusion : exclusions) {
            parameters.remove(exclusion);
        }
        return parameters;
    }

    public static void throwError(String errorCode, String parameterName) {
		throw new IllegalParameterExtensionLoginException(parameterName + "不能为空");
    }

    private static boolean checkRequired(MultiValueMap<String, String> parameters, String parameterName, String parameterValue) {
        return !StringUtils.hasText(parameterValue) || parameters.get(parameterName).size() != 1;
    }

    private static boolean checkOptional(MultiValueMap<String, String> parameters, String parameterName, String parameterValue) {
        return StringUtils.hasText(parameterValue) && parameters.get(parameterName).size() != 1;
    }

    public static String checkParameter(MultiValueMap<String, String> parameters, String parameterName, boolean isRequired, String errorCode) {
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

    public static String checkRequiredParameter(MultiValueMap<String, String> parameters, String parameterName, String errorCode) {
        return checkParameter(parameters, parameterName, true, errorCode);
    }

    public static String checkRequiredParameter(MultiValueMap<String, String> parameters, String parameterName) {
        return checkRequiredParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }

    public static String checkOptionalParameter(MultiValueMap<String, String> parameters, String parameterName, String errorCode) {
        return checkParameter(parameters, parameterName, false, errorCode);
    }

    public static String checkOptionalParameter(MultiValueMap<String, String> parameters, String parameterName) {
        return checkOptionalParameter(parameters, parameterName, OAuth2ErrorCodes.INVALID_REQUEST);
    }
}
