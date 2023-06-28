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

package com.taotao.cloud.auth.biz.authentication.login.extension.phone;

import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.LoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.phone.service.PhoneService;
import com.taotao.cloud.auth.biz.authentication.login.extension.phone.service.PhoneUserDetailsService;
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

public class PhoneLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractLoginFilterConfigurer<
						H, PhoneLoginFilterConfigurer<H>, PhoneAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

    private PhoneUserDetailsService phoneUserDetailsService;

    private PhoneService phoneService;

    private JwtTokenGenerator jwtTokenGenerator;

    public PhoneLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new PhoneAuthenticationFilter(), "/login/phone");
    }

    public PhoneLoginFilterConfigurer<H> phoneUserDetailsService(PhoneUserDetailsService phoneUserDetailsService) {
        this.phoneUserDetailsService = phoneUserDetailsService;
        return this;
    }

    public PhoneLoginFilterConfigurer<H> phoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
        return this;
    }

    public PhoneLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

        PhoneUserDetailsService phoneUserDetailsService = this.phoneUserDetailsService != null
                ? this.phoneUserDetailsService
                : getBeanOrNull(applicationContext, PhoneUserDetailsService.class);
        Assert.notNull(phoneUserDetailsService, "phoneUserDetailsService is required");

        PhoneService phoneService =
                this.phoneService != null ? this.phoneService : getBeanOrNull(applicationContext, PhoneService.class);
        Assert.notNull(phoneService, "phoneService is required");

        return new PhoneAuthenticationProvider(phoneUserDetailsService, phoneService);
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