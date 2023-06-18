/*
 * Copyright 2002-2020 the original author or authors.
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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.service;

import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.Assert;

/**
 * An implementation of an {@link Auth2UserService} that supports standard OAuth 2.0
 * Provider's.
 *
 * @author YongWu zheng
 * @version V1.0  Created by 2020/10/10 7:54
 * @see Auth2UserService
 * @see Auth2DefaultRequest
 * @see AuthUser
 * @since 2.0.0
 */
public class DefaultAuth2UserServiceImpl implements Auth2UserService {

	@Override
	public AuthUser loadUser(Auth2DefaultRequest auth2Request, HttpServletRequest request) throws OAuth2AuthenticationException {

		Assert.notNull(auth2Request, "auth2Request cannot be null");

		AuthCallback authCallback = AuthCallback.builder()
			.code(request.getParameter("code"))
			.state(request.getParameter("state"))
			.auth_code(request.getParameter("auth_code"))
			.authorization_code(request.getParameter("authorization_code"))
			.oauth_token(request.getParameter("oauth_token"))
			.oauth_verifier(request.getParameter("oauth_verifier"))
			.build();

		//noinspection rawtypes
		AuthResponse authResponse = auth2Request.login(authCallback);

		if (authResponse.ok()) {
			AuthUser authUser = (AuthUser) authResponse.getData();
			// 因为原有的 source 不是 camel 风格, 与 providerId 有出入, 覆盖原有的 source 使其与 providerId 字符串一样
			authUser.setSource(auth2Request.getProviderId());

			return authUser;
		} else {
			String msg = authResponse.getMsg();
			OAuth2Error oauth2Error = new OAuth2Error(msg,
				String.format(" for Client Registration: %s", auth2Request.getProviderId()),
				request.getRequestURI());
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

	}

}
