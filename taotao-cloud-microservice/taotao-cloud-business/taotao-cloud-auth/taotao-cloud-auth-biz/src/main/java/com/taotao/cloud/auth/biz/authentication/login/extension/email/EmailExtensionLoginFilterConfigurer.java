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

package com.taotao.cloud.auth.biz.authentication.login.extension.email;

import com.taotao.cloud.auth.biz.authentication.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.email.service.EmailCheckService;
import com.taotao.cloud.auth.biz.authentication.login.extension.email.service.EmailUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.login.extension.sms.SmsAuthenticationFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class EmailExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
	extends AbstractExtensionLoginFilterConfigurer<
	H, EmailExtensionLoginFilterConfigurer<H>, SmsAuthenticationFilter, ExtensionLoginFilterSecurityConfigurer<H>> {

	private EmailUserDetailsService emailUserDetailsService;
	private EmailCheckService emailCheckService;

	private JwtTokenGenerator jwtTokenGenerator;

	public EmailExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new SmsAuthenticationFilter(), "/login/phone");
	}

	public EmailExtensionLoginFilterConfigurer<H> emailUserDetailsService(EmailUserDetailsService emailUserDetailsService) {
		this.emailUserDetailsService = emailUserDetailsService;
		return this;
	}

	public EmailExtensionLoginFilterConfigurer<H> emailCheckService(EmailCheckService emailCheckService) {
		this.emailCheckService = emailCheckService;
		return this;
	}

	public EmailExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

		EmailUserDetailsService emailUserDetailsService = this.emailUserDetailsService != null
			? this.emailUserDetailsService
			: getBeanOrNull(applicationContext, EmailUserDetailsService.class);
		Assert.notNull(emailUserDetailsService, "emailUserDetailsService is required");

		EmailCheckService emailCheckService =
			this.emailCheckService != null ? this.emailCheckService : getBeanOrNull(applicationContext, EmailCheckService.class);
		Assert.notNull(emailCheckService, "emailCheckService is required");

		return new EmailAuthenticationProvider(emailUserDetailsService, emailCheckService);
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
		return new JsonExtensionLoginAuthenticationFailureHandler();
	}

}
