/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.oauth2.biz.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.oauth2.biz.jose.Jwks;
import java.time.Duration;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 */
@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "com.example.springjdbcdemo.infrastruction")
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
		throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
			new OAuth2AuthorizationServerConfigurer<>();
		authorizationServerConfigurer
			.authorizationEndpoint(authorizationEndpoint ->
				authorizationEndpoint.consentPage("/oauth2/consent"));

		RequestMatcher endpointsMatcher = authorizationServerConfigurer
			.getEndpointsMatcher();

		http
			.requestMatcher(endpointsMatcher)
			.authorizeRequests(authorizeRequests ->
				authorizeRequests.anyRequest().authenticated()
			)
			.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
			.apply(authorizationServerConfigurer);
		return http.formLogin(Customizer.withDefaults()).build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId("messaging-client")
			.clientSecret("{noop}secret")
			.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
			.redirectUri("https://www.baidu.com")
			.scope(OidcScopes.OPENID)
			.scope("message.read")
			.scope("message.write")
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.tokenSettings(TokenSettings.builder()
				.accessTokenTimeToLive(Duration.ofMinutes(40000))
				.reuseRefreshTokens(true)
				.refreshTokenTimeToLive(Duration.ofMinutes(60000))
				.build()
			)
			.build();
		return new InMemoryRegisteredClientRepository(registeredClient);
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

//	@Bean
//	public OAuth2AuthorizationConsentService authorizationConsentService() {
//		// Will be used by the ConsentController
//		return new InMemoryOAuth2AuthorizationConsentService();
//	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder()
			.authorizationEndpoint("http://172.16.6.151:33336/oauth2/authorize")
			.tokenEndpoint("http://172.16.6.151:33336/oauth2/token")
			.jwkSetEndpoint("http://172.16.6.151:33336/oauth2/jwks")
			.issuer("http://172.16.6.151:33336")
			.build();
	}

//	@Bean
//	public EmbeddedDatabase embeddedDatabase() {
//		return new EmbeddedDatabaseBuilder()
//			.generateUniqueName(true)
//			.setType(EmbeddedDatabaseType.H2)
//			.setScriptEncoding("UTF-8")
//			.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
//			.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
//			.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
//			.build();
//	}

}
