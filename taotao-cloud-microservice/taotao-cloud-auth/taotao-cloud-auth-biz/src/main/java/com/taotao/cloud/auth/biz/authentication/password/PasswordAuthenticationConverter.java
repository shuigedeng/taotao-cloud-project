package com.taotao.cloud.auth.biz.authentication.password;

import com.taotao.cloud.auth.biz.utils.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.taotao.cloud.auth.biz.models.AuthorizationServerConstant.PARAM_TYPE;
import static com.taotao.cloud.auth.biz.models.AuthorizationServerConstant.VERIFICATION_CODE;

public class PasswordAuthenticationConverter implements AuthenticationConverter {


	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) &&
			parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"scope不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(
				Arrays.asList(StringUtils.delimitedListToStringArray(scope, ",")));
		}

		// 账号 (REQUIRED)
		String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
		if (!StringUtils.hasText(username)
			|| parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"账号不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 密码 (REQUIRED)
		String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
		if (!StringUtils.hasText(password)
			|| parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"密码不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 类型 (REQUIRED)
		String type = parameters.getFirst(PARAM_TYPE);
		if (!StringUtils.hasText(type)
			|| parameters.get(PARAM_TYPE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"登录类型不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 验证码(REQUIRED)
		String code = parameters.getFirst(VERIFICATION_CODE);
		if (!StringUtils.hasText(code)
			|| parameters.get(VERIFICATION_CODE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"验证码不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"客户端认证错误",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Map<String, Object> additionalParameters = parameters
			.entrySet()
			.stream()
			.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) &&
				!e.getKey().equals(OAuth2ParameterNames.SCOPE))
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		return new PasswordAuthenticationToken(
			AuthorizationGrantType.PASSWORD,
			clientPrincipal,
			requestedScopes,
			additionalParameters);

	}

}
