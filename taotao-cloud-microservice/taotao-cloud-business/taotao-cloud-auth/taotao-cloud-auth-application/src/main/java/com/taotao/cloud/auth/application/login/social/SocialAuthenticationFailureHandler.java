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

package com.taotao.cloud.auth.application.login.social;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * LoginAuthenticationSuccessHandler
 *
 * @author shuigedeng
 * @version 2023.07
 * @see AuthenticationFailureHandler
 * @since 2023-07-10 17:40:04
 */
public class SocialAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 关于身份验证失败
     *
     * @param request       请求
     * @param response      响应
     * @param authException 授权例外
     * @since 2023-07-10 17:40:05
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 1.当前有登录用户，跳主页，提示绑定失败（绑定操作）
        // 2.当前无登录用户，跳登录页面（三方登录操作）

        // log.warn("三方授权失败", exception);
        // if(TempAuthContext.notEmpty()){
        //	// 还原已登录用户的认证信息
        //	SecurityContextHolder.getContext().setAuthentication(TempAuthContext.get());
        //	// 绑定失败
        //	getRedirectStrategy().sendRedirect(request, response, WebAttributes.bindFailure);
        //	return;
        // }
        //// 三方登录失败
        // getRedirectStrategy().sendRedirect(request, response, WebAttributes.oauth2LoginError);

        LogUtils.error("用户认证失败", authException);
        ResponseUtils.fail(response, authException.getMessage());
    }
}
