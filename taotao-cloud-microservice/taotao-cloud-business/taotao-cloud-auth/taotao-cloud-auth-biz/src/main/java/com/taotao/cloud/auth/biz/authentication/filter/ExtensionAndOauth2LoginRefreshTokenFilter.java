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

package com.taotao.cloud.auth.biz.authentication.filter;

import com.taotao.cloud.auth.biz.authentication.token.OAuth2AccessTokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 扩展和oauth2登录刷新令牌过滤器
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OncePerRequestFilter
 * @since 2023-07-12 09:17:53
 */
public class ExtensionAndOauth2LoginRefreshTokenFilter extends OncePerRequestFilter {

    private OAuth2AccessTokenStore oAuth2AccessTokenStore;

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    private static final PathPatternRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            PathPatternRequestMatcher.withDefaults()
                    .matcher(HttpMethod.POST, "/login/token/refresh_token");

    public ExtensionAndOauth2LoginRefreshTokenFilter(
            OAuth2AccessTokenStore oAuth2AccessTokenStore) {
        this.oAuth2AccessTokenStore = oAuth2AccessTokenStore;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 刷新token 并且返回新token
        String refreshToken = request.getParameter("refresh_token");

        OAuth2AccessTokenResponse accessTokenResponse =
                oAuth2AccessTokenStore.freshToken(refreshToken);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

    public OAuth2AccessTokenStore getoAuth2AccessTokenStore() {
        return oAuth2AccessTokenStore;
    }

    public void setoAuth2AccessTokenStore(OAuth2AccessTokenStore oAuth2AccessTokenStore) {
        this.oAuth2AccessTokenStore = oAuth2AccessTokenStore;
    }
}
