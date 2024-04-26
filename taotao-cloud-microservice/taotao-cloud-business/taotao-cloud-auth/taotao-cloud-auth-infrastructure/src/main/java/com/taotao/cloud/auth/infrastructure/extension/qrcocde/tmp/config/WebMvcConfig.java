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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.config;

import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.controller.interceptor.ConfirmInterceptor;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.controller.interceptor.LoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    private LoginInterceptor loginInterceptor;

    private ConfirmInterceptor confirmInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/static/**")
                .addPathPatterns("/getUser", "/login/scan");
        registry.addInterceptor(confirmInterceptor)
                .excludePathPatterns("/static/**")
                .addPathPatterns("/login/confirm");
    }
}
