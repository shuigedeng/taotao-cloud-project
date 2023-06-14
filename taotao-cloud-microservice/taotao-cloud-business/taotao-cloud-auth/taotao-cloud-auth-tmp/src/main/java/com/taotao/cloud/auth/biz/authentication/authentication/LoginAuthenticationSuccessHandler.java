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

package com.taotao.cloud.auth.biz.authentication.authentication;

import com.taotao.cloud.auth.biz.authentication.utils.OAuth2ConfigurerUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * LoginAuthenticationSuccessHandler
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-11-07 10:26
 */
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

//	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
//
//	public LoginAuthenticationSuccessHandler(HttpSecurity httpSecurity) {
//		this.tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
//	}

	private JwtTokenGenerator jwtTokenGenerator;

	public LoginAuthenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
		this.jwtTokenGenerator = jwtTokenGenerator;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

//		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
//			.principal(authentication)
//			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
//			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//			.authorizationGrant(authentication);
//
//		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
//		OAuth2Token oAuth2Token = tokenGenerator.generate(tokenContext);
//
//		// OAuth2AccessTokenResponse oAuth2AccessTokenResponse = jwtTokenGenerator.tokenResponse(
//		//	(SecurityUser) authentication.getPrincipal());


		// OAuth2AccessTokenResponse oAuth2AccessTokenResponse = jwtTokenGenerator.tokenResponse(
		//	(SecurityUser) authentication.getPrincipal());

		LogUtils.error("用户认证成功", authentication);
		ResponseUtils.success(response, jwtTokenGenerator.tokenResponse((UserDetails) authentication.getPrincipal()));
	}
}
