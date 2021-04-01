/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.core.model.SecurityUser;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 认证授权相关工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 16:44
 */
@UtilityClass
public class AuthUtil {

	private final String BASIC_ = "Basic ";

//	/**
//	 * 获取request(header/param)中的token
//	 *
//	 * @param request request
//	 * @return java.lang.String
//	 * @author dengtao
//	 * @since 2021/2/25 16:58
//	 */
//	public String extractToken(HttpServletRequest request) {
//		String token = extractHeaderToken(request);
//		if (token == null) {
//			token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
//			if (token == null) {
//				LogUtil.error("Token not found in request parameters.  Not an OAuth2 request.");
//			}
//		}
//		return token;
//	}

	/**
	 * 验证密码
	 *
	 * @param newPass                密码
	 * @param passwordEncoderOldPass 加密后的密码
	 * @return boolean
	 * @author dengtao
	 * @since 2021/2/25 16:58
	 */
	public boolean validatePass(String newPass, String passwordEncoderOldPass) {
		return getPasswordEncoder().matches(newPass, passwordEncoderOldPass);
	}

	/**
	 * 获取密码加密工具
	 *
	 * @return org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
	 * @author dengtao
	 * @since 2021/2/25 16:59
	 */
	public BCryptPasswordEncoder getPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder = BeanUtil.getBean(BCryptPasswordEncoder.class, true);
		if (Objects.isNull(passwordEncoder)) {
			passwordEncoder = new BCryptPasswordEncoder();
		}
		return passwordEncoder;
	}

	/**
	 * 解析head中的token
	 *
	 * @param request request
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:59
	 */
//	private String extractHeaderToken(HttpServletRequest request) {
//		Enumeration<String> headers = request.getHeaders(CommonConstant.TOKEN_HEADER);
//		while (headers.hasMoreElements()) {
//			String value = headers.nextElement();
//			if ((value.startsWith(OAuth2AccessToken.BEARER_TYPE))) {
//				String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length())
//					.trim();
//				int commaIndex = authHeaderValue.indexOf(',');
//				if (commaIndex > 0) {
//					authHeaderValue = authHeaderValue.substring(0, commaIndex);
//				}
//				return authHeaderValue;
//			}
//		}
//		return null;
//	}

	/**
	 * 从header 请求中的clientId:clientSecret
	 *
	 * @param request request
	 * @return java.lang.String[]
	 * @author dengtao
	 * @since 2021/2/25 16:59
	 */
//	public String[] extractClient(HttpServletRequest request) {
//		String header = request.getHeader("BasicAuthorization");
//		if (header == null || !header.startsWith(BASIC_)) {
//			throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
//		}
//		return extractHeaderClient(header);
//	}

	/**
	 * 从header 请求中的clientId:clientSecret
	 *
	 * @param header header中的参数
	 * @return java.lang.String[]
	 * @author dengtao
	 * @since 2021/2/25 16:59
	 */
	public String[] extractHeaderClient(String header) {
		byte[] base64Client = header.substring(BASIC_.length()).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base64.getDecoder().decode(base64Client);
		String clientStr = new String(decoded, StandardCharsets.UTF_8);
		String[] clientArr = clientStr.split(":");
		if (clientArr.length != 2) {
			throw new RuntimeException("Invalid basic authentication token");
		}
		return clientArr;
	}

	/**
	 * 获取登陆的用户名
	 *
	 * @param authentication 认证信息
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:59
	 */
	public String getUsername(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		String username = null;
		if (principal instanceof SecurityUser) {
			username = ((SecurityUser) principal).getUsername();
		} else if (principal instanceof String) {
			username = (String) principal;
		}
		return username;
	}
}
