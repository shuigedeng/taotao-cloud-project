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

package com.taotao.cloud.auth.infrastructure.authentication.response;

import com.taotao.cloud.auth.infrastructure.crypto.HttpCryptoProcessor;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.boot.security.spring.constants.BaseConstants;
import com.taotao.boot.security.spring.constants.HttpHeaders;
import com.taotao.boot.security.spring.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * <p>自定义 Security 认证成功处理器 </p>
 *
 * @since : 2022/2/25 16:53
 */
public class OAuth2AccessTokenResponseHandler implements AuthenticationSuccessHandler {

	private static final Logger log = LoggerFactory.getLogger(
		OAuth2AccessTokenResponseHandler.class);

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
		new OAuth2AccessTokenResponseHttpMessageConverter();

	private final HttpCryptoProcessor httpCryptoProcessor;

	public OAuth2AccessTokenResponseHandler(HttpCryptoProcessor httpCryptoProcessor) {
		this.httpCryptoProcessor = httpCryptoProcessor;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

		log.info("OAuth2 authentication success for [{}]", request.getRequestURI());

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
			(OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(
				accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(
				ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}

		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}

		if (isOidcUserInfoPattern(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		else {
			String sessionId = request.getHeader(HttpHeaders.X_TTC_SESSION);
			Object details = authentication.getDetails();
			if (isTtcUserInfoPattern(sessionId, details)) {
				PrincipalDetails authenticationDetails = (PrincipalDetails) details;
				String data = JsonUtils.toJson(authenticationDetails);
				String encryptData = httpCryptoProcessor.encrypt(sessionId, data);
				Map<String, Object> parameters = new HashMap<>(additionalParameters);
				parameters.put(BaseConstants.OPEN_ID, encryptData);
				builder.additionalParameters(parameters);
			}
			else {
				log.info("OAuth2 authentication can not get use info.");
			}
		}

		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		// this.write(request, response);
		//
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

	private final GenericHttpMessageConverter<Object> jsonMessageConverter = new GsonHttpMessageConverter();
	private final Converter<Map<String, Object>, OAuth2AccessTokenResponse> accessTokenResponseConverter =
		new DefaultMapOAuth2AccessTokenResponseConverter();
	private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter =
		new DefaultOAuth2AccessTokenResponseMapConverter();

	protected void write(HttpServletRequest request, HttpServletResponse httpResponse)
		throws HttpMessageNotReadableException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> tokenResponseParameters = (Map<String, Object>) this.jsonMessageConverter.read(
				STRING_OBJECT_MAP.getType(), null, new ServletServerHttpRequest(request));
			OAuth2AccessTokenResponse tokenResponse =
				this.accessTokenResponseConverter.convert(tokenResponseParameters);
			Map<String, Object> tokenResponseData = this.accessTokenResponseParametersConverter.convert(
				tokenResponse);
			ResponseUtils.success(httpResponse, tokenResponseData);
		}
		catch (Exception ex) {
			throw new HttpMessageNotReadableException(
				"An error occurred reading the OAuth 2.0 Access Token Response: " + ex.getMessage(),
				ex,
				new ServletServerHttpRequest(request));
		}
	}

	private boolean isTtcUserInfoPattern(String sessionId, Object details) {
		return StringUtils.isNotBlank(sessionId)
			&& ObjectUtils.isNotEmpty(details)
			&& details instanceof PrincipalDetails;
	}

	private boolean isOidcUserInfoPattern(Map<String, Object> additionalParameters) {
		return MapUtils.isNotEmpty(additionalParameters)
			&& additionalParameters.containsKey(OidcParameterNames.ID_TOKEN);
	}
}
