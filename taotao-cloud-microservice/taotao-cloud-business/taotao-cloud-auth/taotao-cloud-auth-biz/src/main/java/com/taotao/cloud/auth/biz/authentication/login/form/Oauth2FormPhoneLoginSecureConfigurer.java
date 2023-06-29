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

package com.taotao.cloud.auth.biz.authentication.login.form;

import com.taotao.cloud.auth.biz.authentication.login.form.phone.Oauth2FormPhoneAuthenticationFilter;
import com.taotao.cloud.auth.biz.authentication.login.form.phone.Oauth2FormPhoneAuthenticationProviderOAuth2;
import com.taotao.cloud.auth.biz.authentication.login.form.phone.service.impl.DefaultOauth2FormOauth2FormPhoneService;
import com.taotao.cloud.auth.biz.authentication.login.form.phone.service.impl.DefaultOauth2FormOauth2FormPhoneUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * <p>Description: OAuth2 Form Login Configurer </p>
 * <p>
 * 使用此种方式，相当于额外增加了一种表单登录方式。因此对原有的 http.formlogin进行的配置，对当前此种方式的配置并不生效。
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 16:38:04
 */
public class Oauth2FormPhoneLoginSecureConfigurer<H extends HttpSecurityBuilder<H>>
	extends AbstractHttpConfigurer<Oauth2FormPhoneLoginSecureConfigurer<H>, H> {

	private final OAuth2AuthenticationProperties authenticationProperties;

	public Oauth2FormPhoneLoginSecureConfigurer(OAuth2AuthenticationProperties authenticationProperties) {
		this.authenticationProperties = authenticationProperties;
	}

	@Override
	public void configure(H httpSecurity) throws Exception {
		AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
		SecurityContextRepository securityContextRepository = httpSecurity.getSharedObject(SecurityContextRepository.class);

		Oauth2FormPhoneAuthenticationFilter filter = new Oauth2FormPhoneAuthenticationFilter(authenticationManager);
		filter.setAuthenticationDetailsSource(new OAuth2FormLoginWebAuthenticationDetailSource(authenticationProperties));

		filter.setAuthenticationFailureHandler(new OAuth2FormLoginAuthenticationFailureHandler(getFormLogin().getFailureForwardUrl()));
		filter.setSecurityContextRepository(securityContextRepository);

		Oauth2FormPhoneAuthenticationProviderOAuth2 provider = new Oauth2FormPhoneAuthenticationProviderOAuth2(
			new DefaultOauth2FormOauth2FormPhoneUserDetailsService(),
			new DefaultOauth2FormOauth2FormPhoneService());

		httpSecurity
			.authenticationProvider(provider)
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	}

	private OAuth2AuthenticationProperties.FormLogin getFormLogin() {
		return authenticationProperties.getFormLogin();
	}
}
