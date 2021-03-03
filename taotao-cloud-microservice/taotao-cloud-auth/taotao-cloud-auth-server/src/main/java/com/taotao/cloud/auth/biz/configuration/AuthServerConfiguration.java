/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.auth.biz.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * AuthServerConfiguration
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/03/03 10:17
 */
@Configuration
@EnableWebSecurity
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthServerConfiguration {

	//  定义 spring security 拦击链规则
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorizeRequests ->
				authorizeRequests.anyRequest().authenticated()
			)
			.formLogin(withDefaults());
		return http.build();
	}

	// 创建默认登录用户 lengleng / 123456
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
			.username("lengleng")
			.password("{noop}123456")
			.authorities("ROLE_USER")
			.build();
		return new InMemoryUserDetailsManager(userDetails);
	}

	// 创建默认的bean 登录客户端,基于 授权码、 刷新令牌的能力
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient client = RegisteredClient
			.withId("pig")
			.clientId("pig")
			.clientSecret("pig")
			.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
			.authorizationGrantTypes(authorizationGrantTypes -> {
				authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
				authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
				authorizationGrantTypes.add(AuthorizationGrantType.PASSWORD);
			})
			.redirectUri("https://pig4cloud.com")
			.build();
		return new InMemoryRegisteredClientRepository(client);
	}

	// 指定token 生成的加解密密钥
	@Bean
	@SneakyThrows
	public JWKSource<SecurityContext> jwkSource() {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		// @formatter:off
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
			.privateKey(privateKey)
			.keyID(UUID.randomUUID().toString())
			.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}
}
