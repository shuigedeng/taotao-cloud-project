package com.taotao.cloud.oauth2.api.tmp.social.converter;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.taotao.cloud.oauth2.api.tmp.social.user.WechatOAuth2User;
import java.net.URI;
import java.util.Collections;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 码云 必须得有 User-Agent 微信与标准 Auth2.0 字段名不一样 QQ加这个字段 fmt=json 才是json
 */
public class CustomOAuth2AuthorizationCodeGrantRequestEntityConverter implements
	Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	private static final HttpHeaders DEFAULT_TOKEN_REQUEST_HEADERS = getDefaultTokenRequestHeaders();

	@Override
	public RequestEntity<?> convert(
		OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest
			.getClientRegistration();

		HttpHeaders headers = getTokenRequestHeaders(clientRegistration);
		MultiValueMap<String, String> formParameters = buildFormParameters(
			authorizationCodeGrantRequest);
		URI uri = UriComponentsBuilder
			.fromUriString(clientRegistration.getProviderDetails().getTokenUri())
			.build()
			.toUri();

		return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
	}

	static HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration) {
		HttpHeaders headers = new HttpHeaders();
		headers.addAll(DEFAULT_TOKEN_REQUEST_HEADERS);
		if (ClientAuthenticationMethod.BASIC
			.equals(clientRegistration.getClientAuthenticationMethod())) {
			headers.setBasicAuth(clientRegistration.getClientId(),
				clientRegistration.getClientSecret());
		}
		return headers;
	}

	private static HttpHeaders getDefaultTokenRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		// 码云必须有UA
		headers.add(HttpHeaders.USER_AGENT,
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
		final MediaType contentType = MediaType
			.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		headers.setContentType(contentType);
		return headers;
	}

	private MultiValueMap<String, String> buildFormParameters(
		OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		ClientRegistration clientRegistration = authorizationCodeGrantRequest
			.getClientRegistration();
		OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest
			.getAuthorizationExchange();

		MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
		formParameters.add(OAuth2ParameterNames.GRANT_TYPE,
			authorizationCodeGrantRequest.getGrantType().getValue());
		formParameters.add(OAuth2ParameterNames.CODE,
			authorizationExchange.getAuthorizationResponse().getCode());

		String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
		String codeVerifier = authorizationExchange.getAuthorizationRequest()
			.getAttribute(PkceParameterNames.CODE_VERIFIER);
		if (redirectUri != null) {
			formParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
		}
		if (!ClientAuthenticationMethod.BASIC
			.equals(clientRegistration.getClientAuthenticationMethod())) {
			formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());

		}
		if (ClientAuthenticationMethod.POST
			.equals(clientRegistration.getClientAuthenticationMethod())) {
			formParameters
				.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
			// 微信名字和标准auth2.0 不一样
			formParameters.add(WechatOAuth2User.APP_ID, clientRegistration.getClientId());
			formParameters.add(WechatOAuth2User.SECRET, clientRegistration.getClientSecret());
			// https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
			// QQ 加这个字段才是 json
			formParameters.add("fmt", "json");
		}
		if (codeVerifier != null) {
			formParameters.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
		}

		return formParameters;
	}
}
