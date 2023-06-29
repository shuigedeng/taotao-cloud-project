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

package com.taotao.cloud.auth.biz.authentication.login.extension.accountVerification;

import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.accountVerification.service.AccountVerificationService;
import com.taotao.cloud.auth.biz.authentication.login.extension.accountVerification.service.AccountVerificationUserDetailsService;
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

public class AccountVerificationExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
	extends AbstractExtensionLoginFilterConfigurer<H, AccountVerificationExtensionLoginFilterConfigurer<H>, AccountVerificationAuthenticationFilter, ExtensionLoginFilterSecurityConfigurer<H>> {

	private AccountVerificationUserDetailsService accountVerificationUserDetailsService;

	private AccountVerificationService accountVerificationService;

	private JwtTokenGenerator jwtTokenGenerator;

	public AccountVerificationExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new AccountVerificationAuthenticationFilter(), "/login/account/verification");
	}

	public AccountVerificationExtensionLoginFilterConfigurer<H> accountVerificationUserDetailsService(
		AccountVerificationUserDetailsService accountVerificationUserDetailsService) {
		this.accountVerificationUserDetailsService = accountVerificationUserDetailsService;
		return this;
	}

	public AccountVerificationExtensionLoginFilterConfigurer<H> accountVerificationService(
		AccountVerificationService accountVerificationService) {
		this.accountVerificationService = accountVerificationService;
		return this;
	}

	public AccountVerificationExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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

		AccountVerificationUserDetailsService captchaUserDetailsService =
			this.accountVerificationUserDetailsService != null
				? this.accountVerificationUserDetailsService
				: getBeanOrNull(applicationContext, AccountVerificationUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");

		AccountVerificationService captchaService = this.accountVerificationService != null
			? this.accountVerificationService
			: getBeanOrNull(applicationContext, AccountVerificationService.class);
		Assert.notNull(captchaService, "captchaService is required");

		return new AccountVerificationAuthenticationProvider(captchaUserDetailsService, captchaService);
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
