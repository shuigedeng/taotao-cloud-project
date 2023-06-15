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

package com.taotao.cloud.auth.biz.management.response;

import com.taotao.cloud.auth.biz.authentication.utils.HttpCryptoProcessor;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.security.springsecurity.core.constants.BaseConstants;
import com.taotao.cloud.security.springsecurity.core.constants.HttpHeaders;
import com.taotao.cloud.security.springsecurity.core.definition.domain.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 自定义 Security 认证成功处理器 </p>
 *
 * 
 * @date : 2022/2/25 16:53
 */
public class OAuth2AccessTokenResponseHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AccessTokenResponseHandler.class);

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    private final HttpCryptoProcessor httpCryptoProcessor;

    public OAuth2AccessTokenResponseHandler(HttpCryptoProcessor httpCryptoProcessor) {
        this.httpCryptoProcessor = httpCryptoProcessor;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

       log.info("[Herodotus] |- OAuth2 authentication success for [{}]", request.getRequestURI());

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType())
                        .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }

        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }

        if (isOidcUserInfoPattern(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        } else {
            String sessionId = request.getHeader(HttpHeaders.X_HERODOTUS_SESSION);
            Object details = authentication.getDetails();
            if (isHerodotusUserInfoPattern(sessionId, details)) {
                PrincipalDetails authenticationDetails = (PrincipalDetails) details;
                String data = JsonUtils.toJson(authenticationDetails);
                String encryptData = httpCryptoProcessor.encrypt(sessionId, data);
                Map<String, Object> parameters = new HashMap<>(additionalParameters);
                parameters.put(BaseConstants.OPEN_ID, encryptData);
                builder.additionalParameters(parameters);
            } else {
               log.info("[Herodotus] |- OAuth2 authentication can not get use info.");
            }
        }

        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

    private boolean isHerodotusUserInfoPattern(String sessionId, Object details) {
        return StringUtils.isNotBlank(sessionId) && ObjectUtils.isNotEmpty(details) && details instanceof PrincipalDetails;
    }

    private boolean isOidcUserInfoPattern(Map<String, Object> additionalParameters) {
        return MapUtils.isNotEmpty(additionalParameters) && additionalParameters.containsKey(OidcParameterNames.ID_TOKEN);
    }
}