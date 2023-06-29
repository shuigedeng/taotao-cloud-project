/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.password;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.OAuth2AbstractAuthenticationConverter;
import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.utils.OAuth2EndpointUtils;
import com.taotao.cloud.security.springsecurity.core.constants.HttpHeaders;
import com.taotao.cloud.security.springsecurity.core.definition.HerodotusGrantType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>Description: 自定义密码模式认证转换器 </p>
 * <p>
 * {@code AuthenticationConverter} 类似于以前的 {@code AbstractTokenGranter}
 * 主要用途是从请求中获取参数，并拼装Token类
 *
 * 
 * @date : 2022/2/22 17:03
 */
public final class OAuth2ResourceOwnerPasswordAuthenticationConverter extends OAuth2AbstractAuthenticationConverter {

	public OAuth2ResourceOwnerPasswordAuthenticationConverter(HttpCryptoProcessor httpCryptoProcessor) {
		super(httpCryptoProcessor);
	}

	@Nullable
	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!HerodotusGrantType.PASSWORD.getValue().equals(grantType)) {
			return null;
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = OAuth2EndpointUtils.checkOptionalParameter(parameters, OAuth2ParameterNames.SCOPE);

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(
				Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// username (REQUIRED)
		OAuth2EndpointUtils.checkRequiredParameter(parameters, OAuth2ParameterNames.USERNAME);

		// password (REQUIRED)
		OAuth2EndpointUtils.checkRequiredParameter(parameters, OAuth2ParameterNames.PASSWORD);

		String sessionId = request.getHeader(HttpHeaders.X_HERODOTUS_SESSION);

		Map<String, Object> additionalParameters = new HashMap<>();
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
				!key.equals(OAuth2ParameterNames.SCOPE)) {
				additionalParameters.put(key, (value.size() == 1) ? decrypt(sessionId, value.get(0)) : decrypt(sessionId, value));
			}
		});

		return new OAuth2ResourceOwnerPasswordAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);

	}
}
