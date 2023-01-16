package com.taotao.cloud.auth.biz.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.auth.biz.jwt.Jwks;
import com.taotao.cloud.auth.biz.jwt.JwtCustomizer;
import com.taotao.cloud.auth.biz.jwt.JwtCustomizerServiceImpl;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGeneratorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


/**
 * The Jwt configuration.
 *
 * @author felord.cn
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class JwtConfiguration {

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> buildCustomizer() {
		JwtCustomizer jwtCustomizer = new JwtCustomizerServiceImpl();
		return jwtCustomizer::customizeToken;
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public JwtTokenGenerator jwtTokenGenerator() {
		return new JwtTokenGeneratorImpl();
	}

	///**
	// * 加载JWK资源
	// *
	// * @return the jwk source
	// */
	//@SneakyThrows
	//@Bean
	//public JWKSource<SecurityContext> jwkSource() {
	//    //TODO 这里优化到配置
	//    String path = "jose.jks";
	//    String alias = "jose";
	//    String pass = "felord.cn";
	//
	//    ClassPathResource resource = new ClassPathResource(path);
	//    KeyStore jks = KeyStore.getInstance("jks");
	//    char[] pin = pass.toCharArray();
	//    jks.load(resource.getInputStream(), pin);
	//    RSAKey rsaKey = RSAKey.load(jks, alias, pin);
	//    return (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaKey));
	//}
	//
	///**
	// * Jwt decoder jwt decoder.
	// *
	// * @return the jwt decoder
	// */
	//@SneakyThrows
	//@Bean
	//@ConditionalOnClass(BearerTokenAuthenticationFilter.class)
	//JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
	//    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
	//    JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
	//    jwtProcessor.setJWSKeySelector(keySelector);
	//    // Spring Security validates the claim set independent from Nimbus
	//    jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
	//    });
	//    return new NimbusJwtDecoder(jwtProcessor);
	//}
}
