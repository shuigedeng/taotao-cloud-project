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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social;

import com.taotao.cloud.auth.infrastructure.authentication.extension.OAuth2AbstractAuthenticationConverter;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.enums.AccountType;
import com.taotao.cloud.auth.infrastructure.crypto.HttpCryptoProcessor;
import com.taotao.cloud.auth.infrastructure.utils.OAuth2EndpointUtils;
import com.taotao.boot.security.spring.constants.BaseConstants;
import com.taotao.boot.security.spring.constants.HttpHeaders;
import com.taotao.boot.security.spring.oauth2.TtcAuthorizationGrantType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * <p>社交认证 Converter </p>
 *
 * @since : 2022/3/31 14:16
 */
public class OAuth2SocialCredentialsAuthenticationConverter extends
	OAuth2AbstractAuthenticationConverter {

	public OAuth2SocialCredentialsAuthenticationConverter(HttpCryptoProcessor httpCryptoProcessor) {
		super(httpCryptoProcessor);
	}

	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!TtcAuthorizationGrantType.SOCIAL.getValue().equals(grantType)) {
			return null;
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = OAuth2EndpointUtils.checkOptionalParameter(parameters,
			OAuth2ParameterNames.SCOPE);

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(
				Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// source (REQUIRED)
		String source = OAuth2EndpointUtils.checkRequiredParameter(parameters,
			BaseConstants.SOURCE);
		// others (REQUIRED)
		// TODO：2022-03-31 这里主要是作为参数的检查，社交登录内容比较多，后续根据实际情况添加
		if (StringUtils.hasText(source)) {
			AccountType accountType = AccountType.getAccountType(source);
			if (ObjectUtils.isNotEmpty(accountType)) {
				switch (accountType.getHandler()) {
					case AccountType.PHONE_NUMBER_HANDLER:
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "mobile");
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "code");
						break;
					case AccountType.WECHAT_MINI_APP_HANDLER:
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "appId");
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "sessionKey");
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "encryptedData");
						OAuth2EndpointUtils.checkRequiredParameter(parameters, "iv");
						break;
					default:
						break;
				}
			}
		}

		String sessionId = request.getHeader(HttpHeaders.X_TTC_SESSION);

		Map<String, Object> additionalParameters = new HashMap<>();
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(
				OAuth2ParameterNames.SCOPE)) {
				additionalParameters.put(
					key, (value.size() == 1) ? decrypt(sessionId, value.get(0))
						: decrypt(sessionId, value));
			}
		});

		return new OAuth2SocialCredentialsAuthenticationToken(clientPrincipal, requestedScopes,
			additionalParameters);
	}
}
