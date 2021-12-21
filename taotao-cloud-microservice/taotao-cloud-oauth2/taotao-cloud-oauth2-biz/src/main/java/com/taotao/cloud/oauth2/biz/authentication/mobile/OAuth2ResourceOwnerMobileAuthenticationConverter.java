package com.taotao.cloud.oauth2.biz.authentication.mobile;

import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.oauth2.biz.authentication.OAuth2EndpointUtils;
import com.taotao.cloud.redis.repository.RedisRepository;
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
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class OAuth2ResourceOwnerMobileAuthenticationConverter implements AuthenticationConverter {

	private final RedisRepository redisRepository;

	public static final AuthorizationGrantType MOBILE = new AuthorizationGrantType("mobile");
	public static final String PARAM_MOBILE = "mobile";
	public static final String PARAM_CAPTCHA = "captcha";
	public static final String REDIS_CAPTCHA_CODE = "CAPTCHA:CODE";

	public OAuth2ResourceOwnerMobileAuthenticationConverter(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!MOBILE.getValue().equals(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				OAuth2ParameterNames.SCOPE,
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(
				Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// mobile (REQUIRED)
		String mobile = parameters.getFirst(PARAM_MOBILE);
		if (!StringUtils.hasText(mobile) || parameters.get(PARAM_MOBILE).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				PARAM_MOBILE,
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// verificationCode (REQUIRED)
		String verificationCode = parameters.getFirst(PARAM_CAPTCHA);
		if (!StringUtils.hasText(verificationCode)
			|| parameters.get(PARAM_CAPTCHA).size() != 1) {
			OAuth2EndpointUtils.throwError(
				OAuth2ErrorCodes.INVALID_REQUEST,
				PARAM_CAPTCHA + "不能为空",
				OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 校验验证码
		Object code = redisRepository.get(REDIS_CAPTCHA_CODE);
		if (!verificationCode.equals(code)) {
			HttpServletResponse response = RequestUtil.getHttpServletResponse();
			ResponseUtil.fail(response, "验证码错误");
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

		return new OAuth2ResourceOwnerMobileAuthenticationToken(mobile, MOBILE,
			clientPrincipal, requestedScopes, additionalParameters);

	}

}
