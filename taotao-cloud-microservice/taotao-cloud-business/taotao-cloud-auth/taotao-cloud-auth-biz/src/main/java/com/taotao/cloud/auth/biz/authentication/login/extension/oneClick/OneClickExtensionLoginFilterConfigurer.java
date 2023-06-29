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

package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick;

import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickLoginService;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickJustAuthUserDetailsService;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * 一键登录过滤器配置
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-16 14:51:54
 */
public class OneClickExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
								H, OneClickExtensionLoginFilterConfigurer<H>, OneClickLoginAuthenticationFilter, ExtensionLoginFilterSecurityConfigurer<H>> {

    private OneClickJustAuthUserDetailsService oneClickUserDetailsService;
    private OneClickLoginService oneClickLoginService;
    private JwtTokenGenerator jwtTokenGenerator;

    public OneClickExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new OneClickLoginAuthenticationFilter(),
			"/login/oneclick");
    }

    public OneClickExtensionLoginFilterConfigurer<H> oneClickUserDetailsService(
            OneClickJustAuthUserDetailsService oneClickUserDetailsService) {
        this.oneClickUserDetailsService = oneClickUserDetailsService;
        return this;
    }

	public OneClickExtensionLoginFilterConfigurer<H> oneClickLoginService(
		OneClickLoginService oneClickLoginService) {
		this.oneClickLoginService = oneClickLoginService;
		return this;
	}

    public OneClickExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);

        OneClickJustAuthUserDetailsService oneClickUserDetailsService = this.oneClickUserDetailsService != null
                ? this.oneClickUserDetailsService
                : getBeanOrNull(applicationContext, OneClickJustAuthUserDetailsService.class);
        Assert.notNull(oneClickUserDetailsService, "oneClickUserDetailsService is required");

		OneClickLoginService oneClickLoginService = this.oneClickLoginService != null
			? this.oneClickLoginService
			: getBeanOrNull(applicationContext, OneClickLoginService.class);
		Assert.notNull(oneClickLoginService, "oneClickLoginService is required");

        return new OneClickLoginAuthenticationProvider(oneClickUserDetailsService, oneClickLoginService);
    }

    @Override
    protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
        if (this.jwtTokenGenerator == null) {
            ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
            jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
        }
        Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
        return new JsonExtensionLoginAuthenticationSuccessHandler(jwtTokenGenerator);
    }
	@Override
	protected AuthenticationFailureHandler defaultFailureHandler(H http) {
		return (request, response, authException) -> {
			LogUtils.error("用户认证失败", authException);
			ResponseUtils.fail(response, authException.getMessage());
		};
	}
}
