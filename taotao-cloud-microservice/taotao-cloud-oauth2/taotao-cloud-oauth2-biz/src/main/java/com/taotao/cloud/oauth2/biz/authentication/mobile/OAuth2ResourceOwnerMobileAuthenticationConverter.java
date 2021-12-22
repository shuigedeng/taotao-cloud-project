package com.taotao.cloud.oauth2.biz.authentication.mobile;

import static com.taotao.cloud.oauth2.biz.models.AuthorizationServerConstant.PARAM_MOBILE;
import static com.taotao.cloud.oauth2.biz.models.AuthorizationServerConstant.PARAM_TYPE;
import static com.taotao.cloud.oauth2.biz.models.AuthorizationServerConstant.VERIFICATION_CODE;

import com.taotao.cloud.oauth2.biz.authentication.OAuth2EndpointUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class OAuth2ResourceOwnerMobileAuthenticationConverter implements AuthenticationConverter {

	public static final AuthorizationGrantType MOBILE = new AuthorizationGrantType("mobile");

	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!MOBILE.getValue().equals(grantType)) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"认证类型错误",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"scope不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(
				Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
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

		// 手机号码 (REQUIRED)
		String mobile = parameters.getFirst(PARAM_MOBILE);
		if (!StringUtils.hasText(mobile) || parameters.get(PARAM_MOBILE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"手机号码不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 手机验证码 (REQUIRED)
		String verificationCode = parameters.getFirst(VERIFICATION_CODE);
		if (!StringUtils.hasText(verificationCode)
			|| parameters.get(VERIFICATION_CODE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				"手机验证码不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				OAuth2ErrorCodes.INVALID_CLIENT,
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Map<String, Object> additionalParameters = parameters
			.entrySet()
			.stream()
			.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) &&
				!e.getKey().equals(OAuth2ParameterNames.SCOPE))
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		return new OAuth2ResourceOwnerMobileAuthenticationToken(
			MOBILE,
			clientPrincipal,
			requestedScopes,
			additionalParameters);

	}

}
