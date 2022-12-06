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
package com.taotao.cloud.security.springsecurity.login;

import com.taotao.cloud.common.utils.log.LogUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 登录成功,返回 Token
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/8/25 09:58
 */
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private OAuth2AuthorizedClientRepository authorizedClientRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		LogUtils.info("用户登录成功 {}", authentication);

		OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;

		String clientId = oauth2Authentication.getAuthorizedClientRegistrationId();
		OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientRepository
			.loadAuthorizedClient(
				clientId, oauth2Authentication, request);

		String redirectUrl = (String) request.getSession()
			.getAttribute(OAuth2ParameterNames.REDIRECT_URI);
		redirectUrl += redirectUrl.contains("?") ? "&" : "?";
		response.sendRedirect(
			redirectUrl + OAuth2ParameterNames.ACCESS_TOKEN + "=" + oAuth2AuthorizedClient
				.getAccessToken().getTokenValue());
	}
}
