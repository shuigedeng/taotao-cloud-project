/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.oauth2.api.server.authentication;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.security.token.TaotaoCloudAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机认证过滤器
 * <p>
 * 1.认证请求的方法必须为POST
 * 2.从request中获取手机号
 * 3.封装成自己的Authentication的实现类SmsCodeAuthenticationToken（未认证）
 * 4.调用 AuthenticationManager 的 authenticate 方法进行验证（即MobileAuthenticationProvider
 *
 * @author dengtao
 * @since 2020/4/29 20:23
 * @version 1.0.0
 */
public class TaotaoCloudAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    TaotaoCloudAuthenticationFilter() {
//        super(new AntPathRequestMatcher(CommonConstant.CUSTOM_OAUTH_LOGIN, HttpMethod.POST.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String grantType = obtainParameter(request, CommonConstant.TAOTAO_CLOUD_GRANT_TYPE);
        String userType = obtainParameter(request, CommonConstant.TAOTAO_CLOUD_USER_TYPE);

        TaotaoCloudAuthenticationToken token;
        String principal;
        String credentials;

        if (CommonConstant.MEMBER_USER.equals(userType)) {
            if (CommonConstant.PHONE_LOGIN.equals(grantType)) {
                principal = obtainParameter(request, CommonConstant.PHONE);
                credentials = obtainParameter(request, CommonConstant.VERIFY_CODE);
            } else if (CommonConstant.QR_LOGIN.equals(grantType)) {
                principal = obtainParameter(request, CommonConstant.QR_CODE);
                credentials = "";
            } else {
                principal = obtainParameter(request, CommonConstant.USERNAME);
                credentials = obtainParameter(request, CommonConstant.PASSWORD);
            }
        } else {
            principal = obtainParameter(request, CommonConstant.USERNAME);
            credentials = obtainParameter(request, CommonConstant.PASSWORD);
        }

        if (principal == null) {
            throw new BaseException("登录参数错误");
        }
        principal = principal.trim();

        token = new TaotaoCloudAuthenticationToken(principal, credentials, grantType, userType);
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken token) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter);
    }
}
