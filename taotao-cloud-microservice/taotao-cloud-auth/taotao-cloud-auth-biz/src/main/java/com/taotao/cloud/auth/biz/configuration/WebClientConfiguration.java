package com.taotao.cloud.auth.biz.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClientConfiguration 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-28 11:28:48
 */
@Configuration
public class WebClientConfiguration {

	@Bean
	WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
			new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

		return WebClient
			.builder()
			.apply(oauth2Client.oauth2Configuration())
			.build();
	}

	@Bean
	OAuth2AuthorizedClientManager authorizedClientManager(
		ClientRegistrationRepository clientRegistrationRepository,
		OAuth2AuthorizedClientRepository authorizedClientRepository) {

		OAuth2AuthorizedClientProvider authorizedClientProvider =
			OAuth2AuthorizedClientProviderBuilder.builder()
				.authorizationCode()
				.refreshToken()
				.clientCredentials()
				.password()
				.build();

		DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
			clientRegistrationRepository, authorizedClientRepository);

		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		// Assuming the `username` and `password` are supplied as `HttpServletRequest` parameters,
		// map the `HttpServletRequest` parameters to `OAuth2AuthorizationContext.getAttributes()`
		authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

		return authorizedClientManager;
	}

	private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {

		return authorizeRequest -> {

			Map<String, Object> contextAttributes = Collections.emptyMap();

			HttpServletRequest servletRequest = authorizeRequest.getAttribute(
				HttpServletRequest.class.getName());

			String username = servletRequest.getParameter(OAuth2ParameterNames.USERNAME);
			String password = servletRequest.getParameter(OAuth2ParameterNames.PASSWORD);
			String scope = servletRequest.getParameter(OAuth2ParameterNames.SCOPE);
			if (StringUtils.hasText(scope)) {
				contextAttributes = new HashMap<>();
				contextAttributes.put(OAuth2AuthorizationContext.REQUEST_SCOPE_ATTRIBUTE_NAME,
					StringUtils.delimitedListToStringArray(scope, " "));
			}

			if (!StringUtils.hasText(username) && !StringUtils.hasText(password)) {
				Authentication authentication = authorizeRequest.getPrincipal();
				if (authentication instanceof UsernamePasswordAuthenticationToken) {
					UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
					Object principal = token.getPrincipal();
					username = token.getName();
					password = (String) token.getCredentials();
					if (principal instanceof User) {
						User user = (User) principal;
						username = user.getUsername();
					}
				}
			}

			if (StringUtils.hasText(username) && StringUtils.hasText(password)) {

				if (CollectionUtils.isEmpty(contextAttributes)) {
					contextAttributes = new HashMap<>();
				}

				// `PasswordOAuth2AuthorizedClientProvider` requires both attributes
				contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
				contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);

			}

			return contextAttributes;
		};
	}

}
