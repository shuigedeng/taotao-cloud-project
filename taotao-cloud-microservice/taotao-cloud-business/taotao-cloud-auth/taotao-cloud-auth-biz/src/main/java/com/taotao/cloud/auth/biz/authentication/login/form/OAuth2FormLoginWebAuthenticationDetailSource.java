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

package com.taotao.cloud.auth.biz.authentication.login.form;

import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.security.springsecurity.core.details.FormLoginWebAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * <p>Description: 表单登录 Details 定义 </p>
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 16:37:57
 */
public class OAuth2FormLoginWebAuthenticationDetailSource
        implements AuthenticationDetailsSource<HttpServletRequest, FormLoginWebAuthenticationDetails> {

    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2FormLoginWebAuthenticationDetailSource(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public FormLoginWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new FormLoginWebAuthenticationDetails(
                context,
                authenticationProperties.getFormLogin().getCloseCaptcha(),
                authenticationProperties.getFormLogin().getCaptchaParameter(),
                authenticationProperties.getFormLogin().getCategory());
    }
}
