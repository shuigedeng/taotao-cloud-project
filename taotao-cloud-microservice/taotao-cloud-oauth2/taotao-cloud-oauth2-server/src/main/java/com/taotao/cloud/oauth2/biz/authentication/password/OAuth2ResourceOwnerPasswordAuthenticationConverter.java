package com.taotao.cloud.oauth2.biz.authentication.password;

import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.oauth2.biz.authentication.OAuth2EndpointUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class OAuth2ResourceOwnerPasswordAuthenticationConverter implements AuthenticationConverter {
	public static final String TYPE = "type";

	@Override
	public Authentication convert(HttpServletRequest request) {
		HttpServletResponse response = RequestUtil.getHttpServletResponse();

		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
			ResponseUtil.fail(response, "认证类型错误");
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) &&
			parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			ResponseUtil.fail(response, "scope参数不能为空");
			//OAuth2EndpointUtils.throwError(
			//	OAuth2ErrorCodes.INVALID_REQUEST,
			//	OAuth2ParameterNames.SCOPE,
			//	OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
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
			ResponseUtil.fail(response, "账号不能为空");
			//OAuth2EndpointUtils.throwError(
			//	OAuth2ErrorCodes.INVALID_REQUEST,
			//	OAuth2ParameterNames.USERNAME,
			//	OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 密码 (REQUIRED)
		String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
		if (!StringUtils.hasText(password)
			|| parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
			ResponseUtil.fail(response, "密码不能为空");
			//OAuth2EndpointUtils.throwError(
			//	OAuth2ErrorCodes.INVALID_REQUEST,
			//	OAuth2ParameterNames.PASSWORD,
			//	OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 类型 (REQUIRED)
		String type = parameters.getFirst(TYPE);
		if (!StringUtils.hasText(type)
			|| parameters.get(TYPE).size() != 1) {
			ResponseUtil.fail(response, "登录类型不能为空");
			//OAuth2EndpointUtils.throwError(
			//	OAuth2ErrorCodes.INVALID_REQUEST,
			//	OAuth2ParameterNames.PASSWORD,
			//	OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			ResponseUtil.fail(response, "客户端错误");
			//OAuth2EndpointUtils.throwError(
			//	OAuth2ErrorCodes.INVALID_REQUEST,
			//	OAuth2ErrorCodes.INVALID_CLIENT,
			//	OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Map<String, Object> additionalParameters = parameters
			.entrySet()
			.stream()
			.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) &&
				!e.getKey().equals(OAuth2ParameterNames.SCOPE))
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		return new OAuth2ResourceOwnerPasswordAuthenticationToken(
			AuthorizationGrantType.PASSWORD,
			clientPrincipal,
			requestedScopes,
			additionalParameters);

	}

}
