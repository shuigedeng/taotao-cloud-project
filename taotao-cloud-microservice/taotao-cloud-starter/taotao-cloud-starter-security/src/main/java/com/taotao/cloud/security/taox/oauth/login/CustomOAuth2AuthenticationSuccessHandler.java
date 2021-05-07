package com.taotao.cloud.security.taox.oauth.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 登录成功,返回 Token
 */
@Log4j2
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private OAuth2AuthorizedClientRepository authorizedClientRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		log.info("用户登录成功 {}", authentication);

		OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;

		String clientId = oauth2Authentication.getAuthorizedClientRegistrationId();
		OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientRepository
			.loadAuthorizedClient(
				clientId, oauth2Authentication, request);

		String redirectUrl = (String) request.getSession()
			.getAttribute(OAuth2ParameterNames.REDIRECT_URI);
		redirectUrl += redirectUrl.contains("?") ? "&" : "?";
		response.sendRedirect(
			redirectUrl + OAuth2ParameterNames.ACCESS_TOKEN + "=" + oAuth2AuthorizedClient
				.getAccessToken().getTokenValue());
	}
}
