//package com.taotao.cloud.auth.api.tmp.config;
//
//import static org.springframework.security.oauth2.server.authorization.config.TokenSettings.ACCESS_TOKEN_TIME_TO_LIVE;
//import static org.springframework.security.oauth2.server.authorization.config.TokenSettings.REFRESH_TOKEN_TIME_TO_LIVE;
//import static org.springframework.security.oauth2.server.authorization.config.TokenSettings.REUSE_REFRESH_TOKENS;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import java.time.Duration;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.UUID;
//import javax.annotation.Resource;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//
///**
//
// * @since 0.0.1
// */
//@Configuration(proxyBeanMethods = false)
//@EnableConfigurationProperties(AuthorizationProperties.class)
//public class Oauth2AuthorizationServerConfig {
//
//	@Resource
//	private AuthorizationProperties authorizationProperties;
//
//
//	// @formatter:off
//	@Bean
//	public RegisteredClientRepository registeredClientRepository() {
//		List<RegisteredClient> list = new LinkedList<>();
//
//		for (AuthorizationProperties.Client clientRegistration : authorizationProperties
//			.getClient()) {
//
//			RegisteredClient.Builder builder = RegisteredClient
//				.withId(UUID.randomUUID().toString())
//				.clientId(clientRegistration.getClientId())
//				.clientSecret(clientRegistration.getClientSecret())
//				.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//				.authorizationGrantTypes(authorizationGrantTypes -> {
//					authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//					authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//					authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//					authorizationGrantTypes.add(AuthorizationGrantType.PASSWORD);
//				})
//				.clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
//				.tokenSettings(tokenSettings -> {
//					tokenSettings
//						.settings(settings -> {
//							settings.put(ACCESS_TOKEN_TIME_TO_LIVE, Duration.ofMinutes(1000));
//							settings.put(REUSE_REFRESH_TOKENS, true);
//							settings.put(REFRESH_TOKEN_TIME_TO_LIVE, Duration.ofMinutes(6000));
//						});
//				})
//				.redirectUri(clientRegistration.getRedirectUri());
//
//			clientRegistration.getScope().forEach(builder::scope);
//
//			list.add(builder.build());
//		}
//
//		return new InMemoryRegisteredClientRepository(list.toArray(new RegisteredClient[0]));
//	}
//
//	@Bean
//	public JWKSource<SecurityContext> jwkSource() {
//		RSAKey rsaKey = Jwks.generateRsa();
//		JWKSet jwkSet = new JWKSet(rsaKey);
//		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//	}
//
//	@Bean
//	public ProviderSettings providerSettings() {
//		return new ProviderSettings().issuer("http://127.0.0.1:9000");
//	}
//
//	@Bean
//	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//	}
//
//}
