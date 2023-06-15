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

package com.taotao.cloud.auth.biz.authentication.authentication.oauth2;

import com.taotao.cloud.auth.biz.authentication.authentication.JwtTokenGenerator;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * LoginAuthenticationSuccessHandler
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-11-07 10:26
 */
public class Oauth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private JwtTokenGenerator jwtTokenGenerator;

	public Oauth2LoginAuthenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
		this.jwtTokenGenerator = jwtTokenGenerator;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

		LogUtils.error("第三方用户认证成功", authentication);
		ResponseUtils.success(response, jwtTokenGenerator.tokenResponse((UserDetails) authentication.getPrincipal()));
	}
}