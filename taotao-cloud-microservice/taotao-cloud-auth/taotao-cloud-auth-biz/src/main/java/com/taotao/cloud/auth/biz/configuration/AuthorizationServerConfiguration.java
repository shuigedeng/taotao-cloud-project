package com.taotao.cloud.auth.biz.configuration;

import static com.taotao.cloud.auth.biz.authentication.mobile.OAuth2ResourceOwnerMobileAuthenticationConverter.MOBILE;
import static com.taotao.cloud.auth.biz.models.AuthorizationServerConstant.PARAM_MOBILE;
import static com.taotao.cloud.auth.biz.models.AuthorizationServerConstant.PARAM_TYPE;
import static com.taotao.cloud.auth.biz.models.AuthorizationServerConstant.VERIFICATION_CODE;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.auth.biz.authentication.mobile.OAuth2ResourceOwnerMobileAuthenticationConverter;
import com.taotao.cloud.auth.biz.authentication.mobile.OAuth2ResourceOwnerMobileAuthenticationProvider;
import com.taotao.cloud.auth.biz.authentication.mobile.OAuth2ResourceOwnerMobileAuthenticationToken;
import com.taotao.cloud.auth.biz.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.taotao.cloud.auth.biz.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.taotao.cloud.auth.biz.authentication.password.OAuth2ResourceOwnerPasswordAuthenticationToken;
import com.taotao.cloud.auth.biz.jwt.Jwks;
import com.taotao.cloud.auth.biz.jwt.JwtCustomizer;
import com.taotao.cloud.auth.biz.jwt.JwtCustomizerServiceImpl;
import com.taotao.cloud.auth.biz.service.CaptchaService;
import com.taotao.cloud.auth.biz.service.CloudJdbcOAuth2AuthorizationConsentService;
import com.taotao.cloud.auth.biz.service.CloudOAuth2AuthorizationService;
import com.taotao.cloud.auth.biz.service.SmsService;
import com.taotao.cloud.common.enums.UserTypeEnum;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * AuthorizationServerConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 10:24:33
 */
@Configuration
public class AuthorizationServerConfiguration {

	@Value("${oauth2.token.issuer}")
	private String tokenIssuer;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("memberUserDetailsService")
	private UserDetailsService memberUserDetailsService;

	@Autowired
	@Qualifier("sysUserDetailsService")
	private UserDetailsService sysUserDetailsService;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
		throws Exception {

		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

		http.apply(authorizationServerConfigurer
			.tokenEndpoint(tokenEndpointCustomizer ->
				tokenEndpointCustomizer
					.accessTokenRequestConverter(
						new DelegatingAuthenticationConverter(Arrays.asList(
							new OAuth2AuthorizationCodeAuthenticationConverter(),
							new OAuth2RefreshTokenAuthenticationConverter(),
							new OAuth2ClientCredentialsAuthenticationConverter(),
							new OAuth2ResourceOwnerMobileAuthenticationConverter(),
							new OAuth2ResourceOwnerPasswordAuthenticationConverter()))
					)
					.errorResponseHandler((request, response, authException) -> {
						LogUtil.error("用户认证失败", authException);
						ResponseUtil.fail(response, authException.getMessage());
					})
			)
			.authorizationEndpoint(authorizationEndpointCustomizer ->
				authorizationEndpointCustomizer.consentPage("/oauth2/consent")
			)
			.oidc(oidcCustomizer ->
				oidcCustomizer
					.userInfoEndpoint(userInfoEndpointCustomizer ->
						userInfoEndpointCustomizer
							.userInfoMapper(userInfoMapper -> {
								OidcUserInfoAuthenticationToken authentication = userInfoMapper.getAuthentication();
								JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
								return new OidcUserInfo(principal.getToken().getClaims());
							})
					)
			)
		);

		RequestMatcher authorizationServerConfigurerEndpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

		http
			.requestMatcher(authorizationServerConfigurerEndpointsMatcher)
			.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
			.csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurerEndpointsMatcher))
			.formLogin()
			.and()
			.apply(authorizationServerConfigurer);

		SecurityFilterChain securityFilterChain = http.formLogin(Customizer.withDefaults()).build();

		addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider(http);

		addCustomOAuth2ResourceOwnerMobileAuthenticationProvider(http);

		return securityFilterChain;
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(
		JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository,
		RedisRepository redisRepository,
		JwtDecoder jwtDecoder) {

		JdbcOAuth2AuthorizationService service = new CloudOAuth2AuthorizationService(jdbcTemplate,
			registeredClientRepository, redisRepository, jwtDecoder);

		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
			registeredClientRepository);

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

		//// You will need to write the Mixin for your class so Jackson can marshall it.
		//objectMapper.addMixIn(UserAuthority.class, UserAuthorityMixin.class);
		//objectMapper.addMixIn(CloudUserDetails.class, CloudUserDetailsMixin.class);
		//objectMapper.addMixIn(AuditDeletedDate.class, AuditDeletedDateMixin.class);
		//objectMapper.addMixIn(Long.class, LongMixin.class);

		rowMapper.setObjectMapper(objectMapper);
		service.setAuthorizationRowMapper(rowMapper);

		return service;
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(
		JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository) {

		return new CloudJdbcOAuth2AuthorizationConsentService(jdbcTemplate,
			registeredClientRepository);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().issuer(tokenIssuer).build();
	}


	private void addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider(HttpSecurity http) {
		ProviderSettings providerSettings = http.getSharedObject(ProviderSettings.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(
			OAuth2AuthorizationService.class);
		JwtEncoder jwtEncoder = http.getSharedObject(JwtEncoder.class);

		OAuth2TokenCustomizer jwtCustomizer = http.getSharedObject(OAuth2TokenCustomizer.class);
		//OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = buildCustomizer();

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
			new OAuth2ResourceOwnerPasswordAuthenticationProvider(
				userNameAuthenticationManager(),
				authorizationService,
				jwtEncoder);

		if (jwtCustomizer != null) {
			resourceOwnerPasswordAuthenticationProvider.setJwtCustomizer(jwtCustomizer);
		}

		resourceOwnerPasswordAuthenticationProvider.setProviderSettings(providerSettings);
		// This will add new authentication provider in the list of existing authentication providers.
		http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
	}

	private AuthenticationManager userNameAuthenticationManager() {
		return authentication -> {
			OAuth2ResourceOwnerPasswordAuthenticationToken authenticationToken = (OAuth2ResourceOwnerPasswordAuthenticationToken) authentication;
			Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
			// 账号
			String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
			// 密码
			String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
			// 用户类型
			String type = (String) additionalParameters.get(PARAM_TYPE);
			// 验证码
			String verificationCode = (String) additionalParameters.get(VERIFICATION_CODE);
			String t = (String) additionalParameters.get("t");

			// 校验验证码
			//captchaService.checkCaptcha(verificationCode, t);
			//Object code = redisRepository.get(CAPTCHA_KEY_PREFIX + mobile);
			//if (!verificationCode.equals(code)) {
			//	throw new BadCredentialsException("验证码错误");
			//}

			Authentication clientPrincipal = authenticationToken.getClientPrincipal();

			UserDetails userDetails;
			if (UserTypeEnum.MEMBER.getCode() == Integer.parseInt(type)) {
				userDetails = memberUserDetailsService.loadUserByUsername(username);
			} else {
				userDetails = sysUserDetailsService.loadUserByUsername(username);
			}

			if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
				throw new BadCredentialsException("用户密码不匹配");
			}

			OAuth2ResourceOwnerPasswordAuthenticationToken authenticationResult = new OAuth2ResourceOwnerPasswordAuthenticationToken(
				AuthorizationGrantType.PASSWORD,
				clientPrincipal,
				authenticationToken.getScopes(),
				authenticationToken.getAdditionalParameters(),
				userDetails,
				new NullAuthoritiesMapper().mapAuthorities(userDetails.getAuthorities()));

			authenticationResult.setDetails(authenticationToken.getDetails());
			return authenticationResult;
		};
	}

	private void addCustomOAuth2ResourceOwnerMobileAuthenticationProvider(HttpSecurity http) {
		ProviderSettings providerSettings = http.getSharedObject(ProviderSettings.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(
			OAuth2AuthorizationService.class);
		JwtEncoder jwtEncoder = http.getSharedObject(JwtEncoder.class);

		OAuth2TokenCustomizer jwtCustomizer = http.getSharedObject(OAuth2TokenCustomizer.class);
		//OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = buildCustomizer();

		OAuth2ResourceOwnerMobileAuthenticationProvider resourceOwnerMobileAuthenticationProvider =
			new OAuth2ResourceOwnerMobileAuthenticationProvider(
				mobileAuthenticationManager(),
				authorizationService,
				jwtEncoder);

		if (jwtCustomizer != null) {
			resourceOwnerMobileAuthenticationProvider.setJwtCustomizer(jwtCustomizer);
		}

		resourceOwnerMobileAuthenticationProvider.setProviderSettings(providerSettings);

		http.authenticationProvider(resourceOwnerMobileAuthenticationProvider);
	}

	private AuthenticationManager mobileAuthenticationManager() {
		return authentication -> {
			OAuth2ResourceOwnerMobileAuthenticationToken authenticationToken = (OAuth2ResourceOwnerMobileAuthenticationToken) authentication;
			Authentication clientPrincipal = authenticationToken.getClientPrincipal();

			Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
			// 用户类型
			String type = (String) additionalParameters.get(PARAM_TYPE);
			// 手机号
			String mobile = (String) additionalParameters.get(PARAM_MOBILE);
			// 手机验证码
			String verificationCode = (String) additionalParameters.get(VERIFICATION_CODE);

			// 校验验证码
			//Object code = redisRepository.get(SMS_KEY_PREFIX + mobile);
			//if (!verificationCode.equals(code)) {
			//	throw new BadCredentialsException("验证码错误");
			//}
			//smsService.checkSms(verificationCode, mobile);

			UserDetails userDetails;
			if (UserTypeEnum.MEMBER.getCode() == Integer.valueOf(type)) {
				userDetails = memberUserDetailsService.loadUserByUsername(mobile);
			} else {
				userDetails = sysUserDetailsService.loadUserByUsername(mobile);
			}

			OAuth2ResourceOwnerMobileAuthenticationToken authenticationResult = new OAuth2ResourceOwnerMobileAuthenticationToken(
				MOBILE,
				clientPrincipal,
				authenticationToken.getScopes(),
				authenticationToken.getAdditionalParameters(),
				userDetails,
				new NullAuthoritiesMapper().mapAuthorities(userDetails.getAuthorities()));

			authenticationResult.setDetails(authenticationToken.getDetails());
			return authenticationResult;
		};
	}
}
