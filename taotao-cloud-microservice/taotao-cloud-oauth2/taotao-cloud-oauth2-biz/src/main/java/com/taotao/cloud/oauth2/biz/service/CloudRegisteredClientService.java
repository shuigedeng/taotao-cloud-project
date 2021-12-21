package com.taotao.cloud.oauth2.biz.service;

import java.time.Duration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

public class CloudRegisteredClientService extends JdbcRegisteredClientRepository {

	public CloudRegisteredClientService(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		super.save(registeredClient);
	}

	@Override
	public RegisteredClient findById(String id) {
		return super.findById(id);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return super.findByClientId(clientId);

		//RegisteredClient registeredClient = super.findByClientId(clientId);
		//System.out.println(registeredClient + "==========================");
		//
		//CloudRegisteredClient cloudRegisteredClient = cloudRegisteredClientRepository.findFirstByClientId(
		//	clientId);
		//
		//RegisteredClient.Builder clientBuilder = RegisteredClient.withId(
		//		String.valueOf(cloudRegisteredClient.getId()))
		//	.clientId(cloudRegisteredClient.getClientId())
		//	.clientName(cloudRegisteredClient.getClientName())
		//	.clientSecret(cloudRegisteredClient.getClientSecret())
		//	.clientIdIssuedAt(cloudRegisteredClient.getClientIdIssuedAt());
		//List<ClientAuthenticationMethod> methods = cloudRegisteredClient.getClientAuthenticationMethods();
		//if (Objects.nonNull(methods) && methods.size() > 0) {
		//	methods.forEach(clientBuilder::clientAuthenticationMethod);
		//}
		//List<AuthorizationGrantType> grantTypes = cloudRegisteredClient.getAuthorizationGrantTypes();
		//if (Objects.nonNull(grantTypes) && methods.size() > 0) {
		//	grantTypes.forEach(clientBuilder::authorizationGrantType);
		//}
		//List<String> redirectUris = cloudRegisteredClient.getRedirectUris();
		//if (Objects.nonNull(redirectUris) && methods.size() > 0) {
		//	redirectUris.forEach(clientBuilder::redirectUri);
		//}
		//List<String> scopes = cloudRegisteredClient.getScopes();
		//if (Objects.nonNull(scopes) && methods.size() > 0) {
		//	scopes.forEach(clientBuilder::scope);
		//}
		//clientBuilder.tokenSettings(defaultTokenSetting());
		//return clientBuilder.build();
	}

	private TokenSettings defaultTokenSetting() {
		return TokenSettings.builder()
			.accessTokenTimeToLive(Duration.ofDays(1))
			.refreshTokenTimeToLive(Duration.ofDays(30))
			.reuseRefreshTokens(true)
			.idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
			.build();
	}
}
