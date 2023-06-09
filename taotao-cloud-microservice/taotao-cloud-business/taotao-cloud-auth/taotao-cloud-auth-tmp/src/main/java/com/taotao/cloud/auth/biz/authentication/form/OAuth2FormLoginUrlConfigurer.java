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

package com.taotao.cloud.auth.biz.authentication.form;

import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;

/**
 * <p>Description: Form Login 配置统一配置类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/5/14 9:02
 */
public class OAuth2FormLoginUrlConfigurer {

    private final OAuth2AuthenticationProperties authenticationProperties;

    public OAuth2FormLoginUrlConfigurer(OAuth2AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    public FormLoginConfigurer<HttpSecurity> from(FormLoginConfigurer<HttpSecurity> configurer) {
        configurer
                .loginPage(getFormLogin().getLoginPageUrl())
                .usernameParameter(getFormLogin().getUsernameParameter())
                .passwordParameter(getFormLogin().getPasswordParameter());

        if (StringUtils.isNotBlank(getFormLogin().getFailureForwardUrl())) {
            configurer.failureForwardUrl(getFormLogin().getFailureForwardUrl());
        }
        if (StringUtils.isNotBlank(getFormLogin().getSuccessForwardUrl())) {
            configurer.successForwardUrl(getFormLogin().getSuccessForwardUrl());
        }

        return configurer;
    }

    private OAuth2AuthenticationProperties.FormLogin getFormLogin() {
        return authenticationProperties.getFormLogin();
    }
}
