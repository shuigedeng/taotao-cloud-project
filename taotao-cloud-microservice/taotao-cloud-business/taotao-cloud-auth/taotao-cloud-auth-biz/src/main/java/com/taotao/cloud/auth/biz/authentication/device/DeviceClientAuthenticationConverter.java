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

package com.taotao.cloud.auth.biz.authentication.device;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

/**
 * 获取请求中参数转化为DeviceClientAuthenticationToken
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:23:48
 */
public final class DeviceClientAuthenticationConverter implements AuthenticationConverter {

    /**
     * 设备授权请求匹配器
     */
    private final RequestMatcher deviceAuthorizationRequestMatcher;

    /**
     * 设备访问令牌请求匹配器
     */
    private final RequestMatcher deviceAccessTokenRequestMatcher;

    /**
     * 设备客户端认证转换器
     *
     * @param deviceAuthorizationEndpointUri 设备授权端点uri
     * @return
     * @since 2023-07-10 17:23:49
     */
    public DeviceClientAuthenticationConverter(String deviceAuthorizationEndpointUri) {
        RequestMatcher clientIdParameterMatcher =
                request -> request.getParameter(OAuth2ParameterNames.CLIENT_ID) != null;
        this.deviceAuthorizationRequestMatcher =
                new AndRequestMatcher(
                        PathPatternRequestMatcher.withDefaults()
                                .matcher(HttpMethod.POST, deviceAuthorizationEndpointUri),
                        clientIdParameterMatcher);
        this.deviceAccessTokenRequestMatcher =
                request ->
                        AuthorizationGrantType.DEVICE_CODE
                                        .getValue()
                                        .equals(
                                                request.getParameter(
                                                        OAuth2ParameterNames.GRANT_TYPE))
                                && request.getParameter(OAuth2ParameterNames.DEVICE_CODE) != null
                                && request.getParameter(OAuth2ParameterNames.CLIENT_ID) != null;
    }

    /**
     * 转换
     *
     * @param request 请求
     * @return {@link Authentication }
     * @since 2023-07-10 17:23:49
     */
    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!this.deviceAuthorizationRequestMatcher.matches(request)
                && !this.deviceAccessTokenRequestMatcher.matches(request)) {
            return null;
        }

        // client_id (REQUIRED)
        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)
                || request.getParameterValues(OAuth2ParameterNames.CLIENT_ID).length != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        return new DeviceClientAuthenticationToken(
                clientId, ClientAuthenticationMethod.NONE, null, null);
    }
}
