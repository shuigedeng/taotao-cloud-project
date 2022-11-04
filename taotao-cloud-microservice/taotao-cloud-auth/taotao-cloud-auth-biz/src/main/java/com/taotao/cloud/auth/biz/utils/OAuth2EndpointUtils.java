package com.taotao.cloud.auth.biz.utils;

import com.taotao.cloud.auth.biz.authentication.mobile.MobileAuthenticationToken;
import com.taotao.cloud.auth.biz.authentication.password.PasswordAuthenticationToken;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class OAuth2EndpointUtils {

	public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private OAuth2EndpointUtils() {
	}

	public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			if (values.length > 0) {
				for (String value : values) {
					parameters.add(key, value);
				}
			}
		});
		return parameters;
	}

	public static boolean matchesPkceTokenRequest(HttpServletRequest request) {
		return AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(
			request.getParameter(OAuth2ParameterNames.GRANT_TYPE)) &&
			request.getParameter(OAuth2ParameterNames.CODE) != null &&
			request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
	}

	public static void throwError(String errorCode, String description, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, description, errorUri);
		throw new OAuth2AuthenticationException(error);
	}

	public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
		Authentication authentication) {
		if (Objects.nonNull(authentication)) {
			if (authentication instanceof PasswordAuthenticationToken passwordAuthentication) {

				OAuth2ClientAuthenticationToken clientPrincipal = null;

				if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
					passwordAuthentication.getClientPrincipal().getClass())) {
					clientPrincipal = (OAuth2ClientAuthenticationToken) passwordAuthentication.getClientPrincipal();
				}

				if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
					return clientPrincipal;
				}
			}

			if (authentication instanceof MobileAuthenticationToken mobileAuthenticationToken) {

				OAuth2ClientAuthenticationToken clientPrincipal = null;

				if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
					mobileAuthenticationToken.getClientPrincipal().getClass())) {
					clientPrincipal = (OAuth2ClientAuthenticationToken) mobileAuthenticationToken.getClientPrincipal();
				}

				if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
					return clientPrincipal;
				}
			}
		}

		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}

	public static OAuth2RefreshToken generateRefreshToken(Duration tokenTimeToLive,
		Supplier<String> refreshTokenGenerator) {
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plus(tokenTimeToLive);
		return new OAuth2RefreshToken(refreshTokenGenerator.get(), issuedAt, expiresAt);
	}

}
