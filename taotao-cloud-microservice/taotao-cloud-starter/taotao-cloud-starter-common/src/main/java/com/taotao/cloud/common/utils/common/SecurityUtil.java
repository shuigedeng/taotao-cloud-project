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
package com.taotao.cloud.common.utils.common;

import cn.hutool.core.util.CharsetUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.context.ContextUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * SecurityUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 14:55:47
 */
public class SecurityUtil {

	private SecurityUtil() {
	}

	/**
	 * Basic
	 */
	private static final String BASIC_ = "Basic ";

	/**
	 * 回写数据
	 *
	 * @param result   result
	 * @param response response
	 * @since 2021-09-02 14:55:57
	 */
	public static void writeResponse(Result<?> result, HttpServletResponse response)
		throws IOException {
		response.setCharacterEncoding(CharsetUtil.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(JsonUtil.toJSONString(result));
		printWriter.flush();
	}

	/**
	 * 获取认证信息
	 *
	 * @return 认证信息
	 * @since 2021-09-02 14:56:05
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户信息
	 *
	 * @param authentication 认证信息
	 * @return 用户信息
	 * @since 2021-09-02 14:56:13
	 */
	public static SecurityUser getUser(Authentication authentication) {
		if (Objects.isNull(authentication)) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}

		Object principal = authentication.getPrincipal();
		if (Objects.isNull(principal)) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}

		if (principal instanceof SecurityUser) {
			return (SecurityUser) principal;
		} else if (principal instanceof Map) {
			return JsonUtil.toObject(JsonUtil.toJSONString(principal), SecurityUser.class);
		}

		throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
	}

	/**
	 * 获取用户信息
	 *
	 * @return 用户信息
	 * @since 2021-09-02 14:56:28
	 */
	public static SecurityUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 获取用户姓名
	 *
	 * @return 用户姓名
	 * @since 2021-09-02 14:56:33
	 */
	public static String getUsername() {
		SecurityUser user = getUser();
		if (Objects.isNull(user)) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}
		return user.getUsername();
	}

	/**
	 * 获取用户id
	 *
	 * @return 用户id
	 * @since 2021-09-02 14:56:38
	 */
	public static Long getUserId() {
		SecurityUser user = getUser();
		if (Objects.isNull(user)) {
			throw new BusinessException("用户未登录");
		}
		return user.getUserId();
	}

	///**
	// * 获取客户端id
	// *
	// * @return java.lang.String
	// * @author shuigedeng
	// * @since 2020/10/15 15:55
	// */
	//public String getClientId() {
	//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	//	if (authentication instanceof OAuth2Authentication) {
	//		OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
	//		return auth2Authentication.getOAuth2Request().getClientId();
	//	}
	//	return null;
	//}

	///**
	// * 获取request(header/param)中的token
	// *
	// * @param request request
	// * @return java.lang.String
	// * @author shuigedeng
	// * @since 2021/2/25 16:58
	// */
	//public String extractToken(HttpServletRequest request) {
	//	String token = extractHeaderToken(request);
	//	if (token == null) {
	//		token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
	//		if (token == null) {
	//			LogUtil.error("Token not found in request parameters.  Not an OAuth2 request.");
	//		}
	//	}
	//	return token;
	//}

	/**
	 * 验证密码
	 *
	 * @param newPass                密码
	 * @param passwordEncoderOldPass 加密后的密码
	 * @return 是否成功
	 * @since 2021-09-02 14:57:20
	 */
	public static boolean validatePass(String newPass, String passwordEncoderOldPass) {
		return getPasswordEncoder().matches(newPass, passwordEncoderOldPass);
	}

	/**
	 * 获取密码加密工具
	 *
	 * @return 加密对象
	 * @since 2021-09-02 14:57:28
	 */
	public static BCryptPasswordEncoder getPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder = ContextUtil
			.getBean(BCryptPasswordEncoder.class, true);
		if (Objects.isNull(passwordEncoder)) {
			passwordEncoder = new BCryptPasswordEncoder();
		}
		return passwordEncoder;
	}

	///**
	// * 解析head中的token
	// *
	// * @param request request
	// * @return java.lang.String
	// * @author shuigedeng
	// * @since 2021/2/25 16:59
	// */
	//private String extractHeaderToken(HttpServletRequest request) {
	//	Enumeration<String> headers = request.getHeaders(CommonConstant.TOKEN_HEADER);
	//	while (headers.hasMoreElements()) {
	//		String value = headers.nextElement();
	//		if ((value.startsWith(OAuth2AccessToken.BEARER_TYPE))) {
	//			String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length())
	//				.trim();
	//			int commaIndex = authHeaderValue.indexOf(',');
	//			if (commaIndex > 0) {
	//				authHeaderValue = authHeaderValue.substring(0, commaIndex);
	//			}
	//			return authHeaderValue;
	//		}
	//	}
	//	return null;
	//}

	///**
	// * 从header 请求中的clientId:clientSecret
	// *
	// * @param request request
	// * @return java.lang.String[]
	// * @author shuigedeng
	// * @since 2021/2/25 16:59
	// */
	//public String[] extractClient(HttpServletRequest request) {
	//	String header = request.getHeader("BasicAuthorization");
	//	if (header == null || !header.startsWith(BASIC_)) {
	//		throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
	//	}
	//	return extractHeaderClient(header);
	//}

	/**
	 * 从header 请求中的clientId:clientSecret
	 *
	 * @param header header
	 * @return header参数列表
	 * @since 2021-09-02 14:57:55
	 */
	public static String[] extractHeaderClient(String header) {
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
	 * @return 用户名
	 * @since 2021-09-02 14:58:07
	 */
	public static String getUsername(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		String username = null;
		if (principal instanceof SecurityUser) {
			username = ((SecurityUser) principal).getUsername();
		} else if (principal instanceof String) {
			username = (String) principal;
		}
		return username;
	}

	/**
	 * 获取租户信息
	 *
	 * @return 租户信息
	 * @since 2021-09-02 14:58:20
	 */
	public static String getTenant() {
		// todo
		return "";
	}

	/**
	 * getClientId
	 *
	 * @return ClientId
	 * @since 2021-09-02 14:58:29
	 */
	public static String getClientId() {
		// todo
		return "";
	}
}
