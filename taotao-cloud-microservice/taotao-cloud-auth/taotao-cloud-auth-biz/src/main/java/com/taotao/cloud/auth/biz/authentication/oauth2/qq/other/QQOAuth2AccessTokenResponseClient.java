package com.taotao.cloud.auth.biz.authentication.oauth2.qq.other;

import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class QQOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new TextHtmlHttpMessageConverter());
        }

        return restTemplate;
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest)
            throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        OAuth2AuthorizationExchange oAuth2AuthorizationExchange = authorizationGrantRequest.getAuthorizationExchange();

        // 根据API文档获取请求access_token参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.set("client_id", clientRegistration.getClientId());
        params.set("client_secret", clientRegistration.getClientSecret());
        params.set("code", oAuth2AuthorizationExchange.getAuthorizationResponse().getCode());
        params.set("redirect_uri", oAuth2AuthorizationExchange.getAuthorizationRequest().getRedirectUri());
        params.set("grant_type", "authorization_code");
        String tmpTokenResponse = getRestTemplate().postForObject(clientRegistration.getProviderDetails().getTokenUri(), params, String.class);

        // 从API文档中可以轻易获知解析accessToken的方式
        String[] items = tmpTokenResponse.split("&");
        //http://wiki.connect.qq.com/使用authorization_code获取access_token
        //access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
        String accessToken = items[0].substring(items[0].lastIndexOf("=") + 1);
        Long expiresIn = new Long(items[1].substring(items[1].lastIndexOf("=") + 1));

        Set<String> scopes = new LinkedHashSet<>(oAuth2AuthorizationExchange.getAuthorizationRequest().getScopes());
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(accessTokenType)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .additionalParameters(additionalParameters)
                .build();
    }
}
