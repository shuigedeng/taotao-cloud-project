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

package com.taotao.cloud.auth.infrastructure.authentication.device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;

/**
 * 设备码认证提供者
 *
 * @author shuigedeng
 * @version 2023.07
 * @see DeviceClientAuthenticationToken
 * @see DeviceClientAuthenticationConverter
 * @see OAuth2ClientAuthenticationFilter
 * @since 2023-07-10 17:24:07
 */
@Slf4j
@RequiredArgsConstructor
public final class DeviceClientAuthenticationProvider implements AuthenticationProvider {

	private final RegisteredClientRepository registeredClientRepository;

	/**
	 * 异常说明地址
	 */
	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";

	/**
	 * 验证
	 *
	 * @param authentication 身份验证
	 * @return {@link Authentication }
	 * @since 2023-07-10 17:24:07
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		// 执行时肯定是设备码流程
		DeviceClientAuthenticationToken deviceClientAuthentication = (DeviceClientAuthenticationToken) authentication;

		// 只支持公共客户端
		if (!ClientAuthenticationMethod.NONE.equals(
			deviceClientAuthentication.getClientAuthenticationMethod())) {
			return null;
		}

		// 获取客户端id并查询
		String clientId = deviceClientAuthentication.getPrincipal().toString();
		RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(
			clientId);
		if (registeredClient == null) {
			throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
		}

		if (log.isTraceEnabled()) {
			log.trace("Retrieved registered client");
		}

		// 校验客户端
		if (!registeredClient
			.getClientAuthenticationMethods()
			.contains(deviceClientAuthentication.getClientAuthenticationMethod())) {
			throwInvalidClient("authentication_method");
		}

		if (log.isTraceEnabled()) {
			log.trace("Validated device client authentication parameters");
		}

		if (log.isTraceEnabled()) {
			log.trace("Authenticated device client");
		}

		return new DeviceClientAuthenticationToken(
			registeredClient, deviceClientAuthentication.getClientAuthenticationMethod(), null);
	}

	/**
	 * 支持
	 *
	 * @param authentication 身份验证
	 * @return boolean
	 * @since 2023-07-10 17:24:07
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		// 只处理设备码请求
		return DeviceClientAuthenticationToken.class.isAssignableFrom(authentication);
	}

	/**
	 * 抛出无效客户端
	 *
	 * @param parameterName 参数名称
	 * @since 2023-07-10 17:24:07
	 */
	private static void throwInvalidClient(String parameterName) {
		OAuth2Error error = new OAuth2Error(
			OAuth2ErrorCodes.INVALID_CLIENT,
			"Device client authentication failed: " + parameterName, ERROR_URI);
		throw new OAuth2AuthenticationException(error);
	}
}
