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

package com.taotao.cloud.auth.biz.demo.authentication.form;

import cn.herodotus.engine.oauth2.authentication.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.core.definition.details.FormLoginWebAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * Description: 表单登录 Details 定义
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 10:41
 */
public class OAuth2FormLoginWebAuthenticationDetailSource
        implements AuthenticationDetailsSource<
                HttpServletRequest, FormLoginWebAuthenticationDetails> {

    private final OAuth2UiProperties uiProperties;

    public OAuth2FormLoginWebAuthenticationDetailSource(OAuth2UiProperties uiProperties) {
        this.uiProperties = uiProperties;
    }

    @Override
    public FormLoginWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new FormLoginWebAuthenticationDetails(
                context,
                uiProperties.getCloseCaptcha(),
                uiProperties.getCaptchaParameter(),
                uiProperties.getCategory());
    }
}
