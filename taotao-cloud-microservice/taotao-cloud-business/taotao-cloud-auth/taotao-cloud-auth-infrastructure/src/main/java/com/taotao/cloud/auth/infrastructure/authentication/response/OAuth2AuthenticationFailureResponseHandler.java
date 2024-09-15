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

package com.taotao.cloud.auth.infrastructure.authentication.response;

import com.taotao.cloud.auth.infrastructure.utils.OAuth2EndpointUtils;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.boot.security.spring.exception.SecurityGlobalExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.MultiValueMap;

/**
 * <p>认证失败处理器 </p>
 *
 * @since : 2022/2/19 20:48
 */
public class OAuth2AuthenticationFailureResponseHandler implements AuthenticationFailureHandler {

    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        String deviceCode = parameters.getFirst(OAuth2ParameterNames.DEVICE_CODE);
        // 兼容 Device Grant 错误处理
        // Device Grant 需要 SAS 原始出错信息，如果采用原有 SecurityGlobalExceptionHandler 处理方式，将导致前端获取到错误的错误信息
        if (exception instanceof OAuth2AuthenticationException oauth2Exception && StringUtils.isNotBlank(deviceCode)) {
            OAuth2Error error = oauth2Exception.getError();
            ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
            httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            this.errorHttpResponseConverter.write(error, null, httpResponse);
        } else {
            Result<String> result =
                    SecurityGlobalExceptionHandler.resolveSecurityException(exception, request.getRequestURI());
            // response.setStatus(result.getStatus());
            // WebUtils.renderJson(response, result);
            ResponseUtils.fail(response, result);
        }
    }
}
