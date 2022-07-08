package com.taotao.cloud.auth.biz.authentication.oauth2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.client.RestOperations;

public class DelegateOAuth2AccessTokenResponseClient implements
	OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate;
	private final RestOperations restOperations;

	public DelegateOAuth2AccessTokenResponseClient(
		OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate,
		RestOperations restOperations) {
		this.delegate = delegate;

		this.restOperations = restOperations;
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(
		OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		String registrationId = authorizationGrantRequest.getClientRegistration()
			.getRegistrationId();

		if (ClientProviders.WORK_WECHAT_SCAN_CLIENT.registrationId().equals(registrationId)) {
			//todo 缓存获取token 如果获取不到再请求 并放入缓存  企业微信的token不允许频繁获取
			OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(
				authorizationGrantRequest);
			String code = authorizationGrantRequest.getAuthorizationExchange()
				.getAuthorizationResponse()
				.getCode();

			return OAuth2AccessTokenResponse.withResponse(tokenResponse)
				.additionalParameters(Collections.singletonMap(OAuth2ParameterNames.CODE, code))
				.build();
		}

		if("weibo".equals(registrationId)){
			ClientRegistration clientRegistration=authorizationGrantRequest.getClientRegistration();

			System.out.println("registerId："+clientRegistration.getRegistrationId());
			OAuth2AuthorizationExchange oAuth2AuthorizationExchange=authorizationGrantRequest.getAuthorizationExchange();

			Map<String,String> params=new HashMap<>();
			params.put("client_id",clientRegistration.getClientId());
			params.put("client_secret",clientRegistration.getClientSecret());
			params.put("grant_type",clientRegistration.getAuthorizationGrantType().getValue());
			params.put("code",oAuth2AuthorizationExchange.getAuthorizationResponse().getCode());
			params.put("redirect_uri",oAuth2AuthorizationExchange.getAuthorizationResponse().getRedirectUri());
			System.out.println(params);

			String baseUri=clientRegistration.getProviderDetails().getTokenUri();

			String accessTokenUri=baseUri+ "?client_id={client_id}"+
				"&client_secret={client_secret}"+
				"&grant_type={grant_type}"+
				"&redirect_uri={redirect_uri}"+
				"&code={code}";

			String accessTokenResponse= this.restOperations.postForObject(accessTokenUri,null,String.class,params);

			JSONObject object=JSONObject.parseObject(accessTokenResponse);
			String accessToken=object.getString("access_token");
			String expiresIn=object.getString("expires_in");
			String uid=object.getString("uid");

			Map<String,Object> additionalParameters=new HashMap<>();
			additionalParameters.put("uid",uid);

			return OAuth2AccessTokenResponse.withToken(accessToken)
				.expiresIn(Long.parseLong(expiresIn))
				.tokenType(OAuth2AccessToken.TokenType.BEARER)
				.scopes(oAuth2AuthorizationExchange.getAuthorizationRequest().getScopes())
				.additionalParameters(additionalParameters)
				.build();
		}



		return delegate.getTokenResponse(authorizationGrantRequest);

	}

}
