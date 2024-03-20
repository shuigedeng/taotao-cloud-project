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

package com.taotao.cloud.auth.biz.authentication.login.form.captcha;

import com.taotao.cloud.security.springsecurity.core.utils.SymmetricUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>OAuth2 表单登录过滤器 </p>
 *
 * @since : 2022/4/12 11:08
 */
public class OAuth2FormCaptchaLoginAuthenticationFilter extends
	UsernamePasswordAuthenticationFilter {

	private boolean postOnly = true;

	private static final Logger log = LoggerFactory.getLogger(
		OAuth2FormCaptchaLoginAuthenticationFilter.class);

	public OAuth2FormCaptchaLoginAuthenticationFilter() {
		super();
	}

	public OAuth2FormCaptchaLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response)
		throws AuthenticationException {
		if (this.postOnly && !"POST".equals(request.getMethod())) {
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		OAuth2FormCaptchaLoginAuthenticationToken oAuth2FormCaptchaLoginAuthenticationToken =
			getAuthenticationToken(request);

		// Allow subclasses to set the "details" property
		setDetails(request, oAuth2FormCaptchaLoginAuthenticationToken);

		return this.getAuthenticationManager()
			.authenticate(oAuth2FormCaptchaLoginAuthenticationToken);
	}

	private OAuth2FormCaptchaLoginAuthenticationToken getAuthenticationToken(
		HttpServletRequest request) {

		String username = obtainUsername(request);
		String password = obtainPassword(request);
		String key = request.getParameter("symmetric");

		if (StringUtils.isBlank(username)) {
			username = "";
		}

		if (StringUtils.isBlank(password)) {
			password = "";
		}

		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(username)
			&& StringUtils.isNotBlank(password)) {
			byte[] byteKey = SymmetricUtils.getDecryptedSymmetricKey(key);
			username = SymmetricUtils.decrypt(username, byteKey);
			password = SymmetricUtils.decrypt(password, byteKey);
			log.info("Decrypt Username is : [{}], Password is : [{}]", username, password);
		}

		return new OAuth2FormCaptchaLoginAuthenticationToken(username, password);
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		super.setPostOnly(postOnly);
		this.postOnly = postOnly;
	}
}
