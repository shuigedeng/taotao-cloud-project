//package com.taotao.cloud.auth.api.tmp.social.converter;
//
//import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
//
//import java.net.URI;
//import java.util.Collections;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.RequestEntity;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.AuthenticationMethod;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.util.UriComponentsBuilder;
//
///**
// * 码云 必须得有 User-Agent 微信/QQ 得加上openid参数问题
// */
//public class CustomOAuth2UserRequestEntityConverter implements
//	Converter<OAuth2UserRequest, RequestEntity<?>> {
//
//	private static final MediaType DEFAULT_CONTENT_TYPE = MediaType
//		.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
//
//	/**
//	 * Returns the {@link RequestEntity} used for the UserInfo Request.
//	 *
//	 * @param userRequest the user request
//	 * @return the {@link RequestEntity} used for the UserInfo Request
//	 */
//	@Override
//	public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
//		ClientRegistration clientRegistration = userRequest.getClientRegistration();
//
//		HttpMethod httpMethod = HttpMethod.GET;
//		if (AuthenticationMethod.FORM.equals(
//			clientRegistration.getProviderDetails().getUserInfoEndpoint()
//				.getAuthenticationMethod())) {
//			httpMethod = HttpMethod.POST;
//		}
//		HttpHeaders headers = new HttpHeaders();
//
//		headers.add(HttpHeaders.USER_AGENT,
//			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		URI uri = UriComponentsBuilder
//			.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
//			.build()
//			.toUri();
//
//		RequestEntity<?> request;
//		if (HttpMethod.POST.equals(httpMethod)) {
//			headers.setContentType(DEFAULT_CONTENT_TYPE);
//			MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
//			formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN,
//				userRequest.getAccessToken().getTokenValue());
//			// 解决微信/QQ问题: 得加上openid参数问题
//			if (userRequest.getAdditionalParameters().get("openid") != null) {
//				formParameters
//					.add("openid", (String) userRequest.getAdditionalParameters().get("openid"));
//			}
//			// https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
//			formParameters.add("fmt", "json");
//
//			request = new RequestEntity<>(formParameters, headers, httpMethod, uri);
//		} else {
//			headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
//			request = new RequestEntity<>(headers, httpMethod, uri);
//		}
//
//		return request;
//	}
//}
