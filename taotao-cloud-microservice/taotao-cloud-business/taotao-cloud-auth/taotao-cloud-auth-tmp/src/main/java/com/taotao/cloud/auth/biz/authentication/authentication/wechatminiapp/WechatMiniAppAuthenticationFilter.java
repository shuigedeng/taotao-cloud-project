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

package com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp.service.WechatMiniAppRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;

public class WechatMiniAppAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login/miniapp", "POST");
    private final ObjectMapper om = new ObjectMapper();
    private Converter<HttpServletRequest, WechatMiniAppAuthenticationToken> miniAppAuthenticationTokenConverter;
    private boolean postOnly = true;

    public WechatMiniAppAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.miniAppAuthenticationTokenConverter = defaultConverter();
    }

    public WechatMiniAppAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.miniAppAuthenticationTokenConverter = defaultConverter();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        WechatMiniAppAuthenticationToken authRequest = miniAppAuthenticationTokenConverter.convert(request);
        if (authRequest == null) {
            throw new BadCredentialsException("fail to extract miniapp authentication request params");
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, WechatMiniAppAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setConverter(Converter<HttpServletRequest, WechatMiniAppAuthenticationToken> converter) {
        Assert.notNull(converter, "Converter must not be null");
        this.miniAppAuthenticationTokenConverter = converter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    private Converter<HttpServletRequest, WechatMiniAppAuthenticationToken> defaultConverter() {
        return request -> {
            try (BufferedReader reader = request.getReader()) {
                WechatMiniAppRequest wechatMiniAppRequest = this.om.readValue(reader, WechatMiniAppRequest.class);
                return new WechatMiniAppAuthenticationToken(wechatMiniAppRequest);
            } catch (IOException e) {
                return null;
            }
        };
    }
}
