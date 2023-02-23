package com.taotao.cloud.auth.biz.authentication.oauth2.qq.other;

import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2AccessTokenResponseClient的组合类，使用了Composite Pattern（组合模式）
 * 除了同时支持GOOGLE，OKTA，GITHUB，FACEBOOK之外，可能还需要同时支持QQ、微信等多种认证服务
 * 根据registrationId选择相应的OAuth2AccessTokenResponseClient
 */
public class CompositeOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private Map<String, OAuth2AccessTokenResponseClient> clients;

    private static final String DefaultClientKey = "default_key";

    public CompositeOAuth2AccessTokenResponseClient() {
        this.clients = new HashMap<>();
        // spring-security-oauth2-client默认的OAuth2AccessTokenResponseClient实现类是NimbusAuthorizationCodeTokenResponseClient
        // 将其预置到组合类CompositeOAuth2AccessTokenResponseClient中，从而默认支持GOOGLE，OKTA，GITHUB，FACEBOOK
        this.clients.put(DefaultClientKey, new DefaultAuthorizationCodeTokenResponseClient());
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest)
            throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

        OAuth2AccessTokenResponseClient client = clients.get(clientRegistration.getRegistrationId());
        if (client == null) {
            client = clients.get(DefaultClientKey);
        }

        return client.getTokenResponse(authorizationGrantRequest);
    }


    public Map<String, OAuth2AccessTokenResponseClient> getOAuth2AccessTokenResponseClients() {
        return clients;
    }
}
