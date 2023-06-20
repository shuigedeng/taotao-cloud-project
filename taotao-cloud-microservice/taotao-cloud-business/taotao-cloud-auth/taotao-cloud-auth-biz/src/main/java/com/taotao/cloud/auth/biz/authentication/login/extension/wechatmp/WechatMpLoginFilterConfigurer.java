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

package com.taotao.cloud.auth.biz.authentication.login.extension.wechatmp;

import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.LoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatmp.service.WechatMpUserDetailsService;
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

public class WechatMpLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractLoginFilterConfigurer<
						H, WechatMpLoginFilterConfigurer<H>, WechatMpAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

    private WechatMpUserDetailsService wechatMpUserDetailsService;

    private JwtTokenGenerator jwtTokenGenerator;

    public WechatMpLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new WechatMpAuthenticationFilter(), "/login/mp");
    }

    public WechatMpLoginFilterConfigurer<H> mpUserDetailsService(WechatMpUserDetailsService wechatMpUserDetailsService) {
        this.wechatMpUserDetailsService = wechatMpUserDetailsService;
        return this;
    }

    public WechatMpLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        WechatMpUserDetailsService wechatMpUserDetailsService = this.wechatMpUserDetailsService != null
                ? this.wechatMpUserDetailsService
                : getBeanOrNull(applicationContext, WechatMpUserDetailsService.class);
        Assert.notNull(wechatMpUserDetailsService, "mpUserDetailsService is required");

        return new WechatMpAuthenticationProvider(wechatMpUserDetailsService);
    }

    @Override
    protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
        if (this.jwtTokenGenerator == null) {
            ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
            jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
        }
        Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
        return new LoginAuthenticationSuccessHandler(jwtTokenGenerator);
    }
	@Override
	protected AuthenticationFailureHandler defaultFailureHandler(H http) {
		return (request, response, authException) -> {
			LogUtils.error("用户认证失败", authException);
			ResponseUtils.fail(response, authException.getMessage());
		};
	}
}
