///*
// * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2.configuration;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//import com.taotao.cloud.oauth2.api.oauth2.security.CustomizedAccessDeniedHandler;
//import com.taotao.cloud.oauth2.api.oauth2.security.CustomizedAuthenticationEntryPoint;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationManagerResolver;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
//import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
//import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
//import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
//import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.util.Assert;
//
///**
// * DefaultSecurityConfig
// *
// * @author shuigedeng
// * @version 2022.03
// * @since 2021/03/30 18:01
// */
//@EnableWebSecurity
//public class Oauth2ResourceServerConfiguration {
//
//	@Autowired
//	private OAuth2ClientProperties oAuth2ClientProperties;
//
////	@Bean
////	WebSecurityCustomizer webSecurityCustomizer() {
////		return (web) -> web.ignoring().antMatchers("/webjars/**", "/login");
////	}
//
//	@Bean
//	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests(authorizeRequests -> authorizeRequests
////				.mvcMatchers("/messages/**").access("hasAuthority('SCOPE_message.read')")
//					.anyRequest().authenticated()
//			)
//			.formLogin(formLogin -> {
//
//			})
////			.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
////				.authenticationEntryPoint(new CustomizedAuthenticationEntryPoint())
////				.accessDeniedHandler(new CustomizedAccessDeniedHandler())
////			)
//			.csrf().disable()
//			.anonymous()
//			.and()
//			.oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
//					.accessDeniedHandler(new CustomizedAccessDeniedHandler())
//					.authenticationEntryPoint(new CustomizedAuthenticationEntryPoint())
////					.authenticationManagerResolver(customAuthenticationManager())
//					.jwt()
//			);
//
//		return http.build();
//	}
//
//
//	AuthenticationManagerResolver<HttpServletRequest> customAuthenticationManager() {
//		LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers = new LinkedHashMap<>();
//
//		// USE JWT tokens (locally validated) to validate HEAD, GET, and OPTIONS requests
//		List<String> readMethod = Arrays.asList("HEAD", "GET", "OPTIONS");
//		RequestMatcher readMethodRequestMatcher = request -> readMethod
//			.contains(request.getMethod());
//		authenticationManagers.put(readMethodRequestMatcher, jwt());
//
//		// all other requests will use opaque tokens (remotely validated)
//		RequestMatchingAuthenticationManagerResolver authenticationManagerResolver
//			= new RequestMatchingAuthenticationManagerResolver(authenticationManagers);
//
//		// Use opaque tokens (remotely validated) for all other requests
//		authenticationManagerResolver.setDefaultAuthenticationManager(opaque());
//		return authenticationManagerResolver;
//	}
//
//	// Mimic the default configuration for JWT validation.
////	AuthenticationManager jwt() {
////		// this is the keys endpoint for okta
////		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
////		String jwkSetUri = issuer + "/v1/keys";
////
////		// This is basically the default jwt logic
////		JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
////		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
////		authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
////		return authenticationProvider::authenticate;
////	}
//
//	AuthenticationManager jwt() {
//		// this is the keys endpoint for okta
//		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
//		String jwkSetUri = issuer + "/v1/keys";
//
//		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//
//		// okta recommends validating the `iss` and `aud` claims
//		// see: https://developer.okta.com/docs/guides/validate-access-tokens/java/overview/
//		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
//		validators.add(new JwtTimestampValidator());
//		// Add validation of the issuer claim
//		validators.add(new JwtIssuerValidator(issuer));
//		validators.add(token -> {
//			Set<String> expectedAudience = new HashSet<>();
//			// Add validation of the audience claim
//			expectedAudience.add("api://default");
//			// For new Okta orgs, the default audience is `api://default`,
//			// if you have changed this from the default update this value
//			return !Collections.disjoint(token.getAudience(), expectedAudience)
//				? OAuth2TokenValidatorResult.success()
//				: OAuth2TokenValidatorResult.failure(new OAuth2Error(
//					OAuth2ErrorCodes.INVALID_REQUEST,
//					"This aud claim is not equal to the configured audience",
//					"https://tools.ietf.org/html/rfc6750#section-3.1"));
//		});
//		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(validators);
//		jwtDecoder.setJwtValidator(validator);
//
//		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(
//			jwtDecoder);
//		authenticationProvider
//			.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
//		return authenticationProvider::authenticate;
//	}
//
//	// Mimic the default configuration for opaque token validation
//	AuthenticationManager opaque() {
//		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
//		String introspectionUri = issuer + "/v1/introspect";
//
//		// The default opaque token logic
//		OAuth2ClientProperties.Registration oktaRegistration = oAuth2ClientProperties
//			.getRegistration().get("okta");
//		OpaqueTokenIntrospector introspectionClient = new NimbusOpaqueTokenIntrospector(
//			introspectionUri,
//			oktaRegistration.getClientId(),
//			oktaRegistration.getClientSecret());
//		return new OpaqueTokenAuthenticationProvider(introspectionClient)::authenticate;
//	}
//
//
//	public static class RequestMatchingAuthenticationManagerResolver implements
//		AuthenticationManagerResolver<HttpServletRequest> {
//
//		private final LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers;
//
//		private AuthenticationManager defaultAuthenticationManager = authentication -> {
//			throw new AuthenticationServiceException("Cannot authenticate " + authentication);
//		};
//
//		public RequestMatchingAuthenticationManagerResolver
//			(LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers) {
//			Assert.notEmpty(authenticationManagers, "authenticationManagers cannot be empty");
//			this.authenticationManagers = authenticationManagers;
//		}
//
//		@Override
//		public AuthenticationManager resolve(HttpServletRequest context) {
//			for (Map.Entry<RequestMatcher, AuthenticationManager> entry : this.authenticationManagers
//				.entrySet()) {
//				if (entry.getKey().matches(context)) {
//					return entry.getValue();
//				}
//			}
//
//			return this.defaultAuthenticationManager;
//		}
//
//		public void setDefaultAuthenticationManager(
//			AuthenticationManager defaultAuthenticationManager) {
//			Assert.notNull(defaultAuthenticationManager,
//				"defaultAuthenticationManager cannot be null");
//			this.defaultAuthenticationManager = defaultAuthenticationManager;
//		}
//	}
//}
