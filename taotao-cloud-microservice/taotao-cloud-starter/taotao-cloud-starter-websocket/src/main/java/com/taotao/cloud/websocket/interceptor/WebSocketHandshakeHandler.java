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
package com.taotao.cloud.websocket.interceptor;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.websocket.domain.WebSocketPrincipal;
import com.taotao.cloud.websocket.properties.CustomWebSocketProperties;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * 设置认证用户信息
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 10:59:20
 */
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

	private CustomWebSocketProperties customWebSocketProperties;

	public void setWebSocketProperties(CustomWebSocketProperties customWebSocketProperties) {
		this.customWebSocketProperties = customWebSocketProperties;
	}

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
		Map<String, Object> attributes) {

		Principal principal = request.getPrincipal();
		if (ObjectUtils.isNotEmpty(principal)) {
			LogUtils.info("Get user principal from request, value is  [{}].",
				principal.getName());
			return principal;
		}

		Object user = null;
		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
		if (ObjectUtils.isNotEmpty(httpServletRequest)) {
			user = httpServletRequest.getAttribute(customWebSocketProperties.getPrincipalAttribute());
			if (ObjectUtils.isEmpty(user)) {
				user = httpServletRequest.getParameter(customWebSocketProperties.getPrincipalAttribute());
				if (ObjectUtils.isEmpty(user)) {
					user = httpServletRequest.getHeader("X-taotao-Session");
				} else {
					LogUtils.info(
						"Get user principal [{}] from request parameter, use parameter  [{}]..",
						user, customWebSocketProperties.getPrincipalAttribute());
				}
			} else {
				LogUtils.info(
					"Get user principal [{}] from request attribute, use attribute  [{}]..",
					user, customWebSocketProperties.getPrincipalAttribute());
			}
		}

		if (ObjectUtils.isEmpty(user)) {
			HttpSession httpSession = getSession(request);
			if (ObjectUtils.isNotEmpty(httpSession)) {
				user = httpSession.getAttribute(customWebSocketProperties.getPrincipalAttribute());
				if (ObjectUtils.isEmpty(user)) {
					user = httpSession.getId();
				} else {
					LogUtils.info(
						"Get user principal [{}] from httpsession, use attribute  [{}].",
						user, customWebSocketProperties.getPrincipalAttribute());
				}
			} else {
				LogUtils.error("Cannot find session from websocket request.");
				return null;
			}
		} else {
			LogUtils.info(
				"Get user principal [{}] from request header X_TAOTAO_SESSION.",
				user);
		}

		return new WebSocketPrincipal((String) user);
	}

	private HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			return serverRequest.getServletRequest();
		}

		return null;
	}

	private HttpSession getSession(ServerHttpRequest request) {
		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
		if (ObjectUtils.isNotEmpty(httpServletRequest)) {
			return httpServletRequest.getSession(false);
		}
		return null;
	}
}
