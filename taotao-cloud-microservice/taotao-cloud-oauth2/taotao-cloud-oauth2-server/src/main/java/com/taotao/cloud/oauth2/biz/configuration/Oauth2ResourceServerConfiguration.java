package com.taotao.cloud.oauth2.biz.configuration;

import com.taotao.cloud.oauth2.biz.models.CustomJwtGrantedAuthoritiesConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	order = 0
)
@EnableWebSecurity
public class Oauth2ResourceServerConfiguration {

	@Value("${jwk.set.uri}")
	private String jwkSetUri;

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	JwtAuthenticationConverter jwtAuthenticationConverter() {
		CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.mvcMatcher("/messages/**").authorizeRequests()
			.anyRequest().authenticated()
			//.mvcMatchers("/messages/**").access("hasAuthority('USER')")
			.and()
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
			)
			.anonymous().disable()
			.csrf().disable().headers().frameOptions().sameOrigin();

		return http.build();
	}

}
