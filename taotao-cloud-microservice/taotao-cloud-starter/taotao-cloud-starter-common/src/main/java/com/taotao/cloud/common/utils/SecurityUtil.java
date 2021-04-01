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

import cn.hutool.core.util.CharsetUtil;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:39
 */
@UtilityClass
public class SecurityUtil {

	/**
	 * 回写数据
	 *
	 * @param result   result
	 * @param response response
	 * @author dengtao
	 * @since 2020/10/15 15:54
	 */
	public void writeResponse(Result<?> result, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding(CharsetUtil.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(JsonUtil.toJSONString(result));
		printWriter.flush();
	}

	/**
	 * 获取认证信息
	 *
	 * @return org.springframework.security.core.Authentication
	 * @author dengtao
	 * @since 2020/10/15 15:54
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户信息
	 *
	 * @param authentication 认证信息
	 * @return com.taotao.cloud.core.model.SecurityUser
	 * @author dengtao
	 * @since 2020/10/15 15:54
	 */
	public SecurityUser getUser(Authentication authentication) {
		if (Objects.isNull(authentication)) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof SecurityUser) {
			return (SecurityUser) principal;
		} else if (principal instanceof Map) {
			return JsonUtil.toObject(JsonUtil.toJSONString(principal), SecurityUser.class);
		}
		return null;
	}

	/**
	 * 获取用户信息
	 *
	 * @return com.taotao.cloud.core.model.SecurityUser
	 * @author dengtao
	 * @since 2020/10/15 15:55
	 */
	public SecurityUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 获取用户姓名
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 15:55
	 */
	public String getUsername() {
		SecurityUser user = getUser();
		return Objects.isNull(user) ? "" : user.getUsername();
	}

	/**
	 * 获取用户id
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 15:55
	 */
	public Long getUserId() {
		SecurityUser user = getUser();
		return Objects.isNull(user) ? null : user.getUserId();
	}

	/**
	 * 获取客户端id
	 *
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 15:55
	 */
//	public String getClientId() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof OAuth2Authentication) {
//			OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
//			return auth2Authentication.getOAuth2Request().getClientId();
//		}
//		return null;
//	}
}
