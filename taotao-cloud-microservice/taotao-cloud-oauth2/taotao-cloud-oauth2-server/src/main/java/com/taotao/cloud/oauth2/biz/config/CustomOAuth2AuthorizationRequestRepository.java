package com.taotao.cloud.oauth2.biz.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * 获取 额外 字段
 */
public final class CustomOAuth2AuthorizationRequestRepository implements
	AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository =
		new HttpSessionOAuth2AuthorizationRequestRepository();

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return authorizationRequestRepository.loadAuthorizationRequest(request);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request, HttpServletResponse response) {
		authorizationRequestRepository
			.saveAuthorizationRequest(authorizationRequest, request, response);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		OAuth2AuthorizationRequest originalRequest = authorizationRequestRepository
			.removeAuthorizationRequest(request);
		request.getSession().setAttribute(OAuth2ParameterNames.REDIRECT_URI,
			originalRequest.getAttributes().get(OAuth2ParameterNames.REDIRECT_URI));
		return originalRequest;
	}

}
