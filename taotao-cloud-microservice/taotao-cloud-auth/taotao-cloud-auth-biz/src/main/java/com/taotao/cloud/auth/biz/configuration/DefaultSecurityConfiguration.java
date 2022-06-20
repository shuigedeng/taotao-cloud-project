package com.taotao.cloud.auth.biz.configuration;

import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
import static org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppClient;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppRequest;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppSessionKeyCache;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.oauth2.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.oauth2.OAuth2ProviderConfigurer;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.models.CustomJwtGrantedAuthoritiesConverter;
import com.taotao.cloud.auth.biz.authentication.oauth2.qq.QQOauth2UserService;
import com.taotao.cloud.auth.biz.service.MemberUserDetailsService;
import com.taotao.cloud.auth.biz.service.SysUserDetailsService;
import com.taotao.cloud.auth.biz.utils.RedirectLoginAuthenticationSuccessHandler;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;


/**
 * SecurityConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 10:20:47
 */
@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	order = 0,
	mode = AdviceMode.PROXY,
	proxyTargetClass = false
)
@EnableWebSecurity
public class DefaultSecurityConfiguration implements EnvironmentAware {

	public static final String[] permitAllUrls = new String[]{
		"/swagger-ui.html",
		"/v3/**",
		"/favicon.ico",
		"/swagger-resources/**",
		"/webjars/**",
		"/actuator/**",
		"/index",
		"/index.html",
		"/auth/captcha/code",
		"/auth/qrcode/code",
		"/auth/sms/phone",
		"/doc.html",
		"/*.js",
		"/*.css",
		"/*.json",
		"/*.min.js",
		"/*.min.css",
		"/health/**"};

	private Environment environment;

	@Primary
	@Bean(name = "memberUserDetailsService")
	public UserDetailsService memberUserDetailsService() {
		return new MemberUserDetailsService();
	}

	@Bean(name = "sysUserDetailsService")
	public UserDetailsService sysUserDetailsService() {
		return new SysUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder
			.userDetailsService(memberUserDetailsService())
			.passwordEncoder(passwordEncoder())
			.and()
			.eraseCredentials(true);
	}

	@Value("${jwk.set.uri}")
	private String jwkSetUri;

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.antMatchers(permitAllUrls)
			.antMatchers("/webjars/**", "/user/login", "/login-error", "/index");
	}

	private String exceptionMessage(AuthenticationException exception) {
		String msg = "访问未授权";
		if (exception instanceof AccountExpiredException) {
			msg = "账户过期";
		} else if (exception instanceof AuthenticationCredentialsNotFoundException) {
			msg = "用户身份凭证未找到";
		} else if (exception instanceof AuthenticationServiceException) {
			msg = "用户身份认证服务异常";
		} else if (exception instanceof BadCredentialsException) {
			msg = exception.getMessage();
		}

		return msg;
	}

	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;

	AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
		LogUtil.error("用户认证失败", authException);
		authException.printStackTrace();
		ResponseUtil.fail(response, exceptionMessage(authException));
	};
	AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		LogUtil.error("用户权限不足", accessDeniedException);
		ResponseUtil.fail(response, ResultEnum.FORBIDDEN);
	};
	AuthenticationFailureHandler authenticationFailureHandler = (request, response, accessDeniedException) -> {
		LogUtil.error("用户权限不足", accessDeniedException);
	};
	AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
		LogUtil.error("用户认证成功", authentication);
		ResponseUtil.success(response,
			jwtTokenGenerator.tokenResponse((UserDetails) authentication.getPrincipal()));
	};

	@Bean
	DelegateClientRegistrationRepository delegateClientRegistrationRepository(
		@Autowired(required = false) OAuth2ClientProperties properties) {
		DelegateClientRegistrationRepository clientRegistrationRepository = new DelegateClientRegistrationRepository();
		if (properties != null) {
			List<ClientRegistration> registrations = new ArrayList<>(
				OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties)
					.values());
			registrations.forEach(clientRegistrationRepository::addClientRegistration);
		}
		return clientRegistrationRepository;
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
		DelegateClientRegistrationRepository delegateClientRegistrationRepository)
		throws Exception {
		http
			.formLogin(formLoginConfigurer -> {
				formLoginConfigurer
					.loginPage("/login")
					.successHandler(new RedirectLoginAuthenticationSuccessHandler())
					.failureHandler(authenticationFailureHandler).permitAll()
					.usernameParameter("username")
					.passwordParameter("password");
			})
			//.userDetailsService(oAuth2UserDetailsService::loadOAuth2UserByUsername)
			.anonymous()
			.and()
			.csrf().disable()
			.logout()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and()
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
					.antMatchers(permitAllUrls).permitAll()
					.mvcMatchers("/user/login", "/login-error", "/index").permitAll()
					.mvcMatchers("/messages/**").access("hasAuthority('ADMIN')")
					.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2ResourceServerCustomizer ->
				oauth2ResourceServerCustomizer
					.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(authenticationEntryPoint)
					.bearerTokenResolver(bearerTokenResolver -> {
						DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
						defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
						defaultBearerTokenResolver.setAllowUriQueryParameter(true);
						return defaultBearerTokenResolver.resolve(bearerTokenResolver);
					})
					.jwt(jwtCustomizer ->
						jwtCustomizer
							.decoder(jwtDecoder())
							.jwtAuthenticationConverter(jwtAuthenticationConverter())
					)
			)
			.apply(new LoginFilterSecurityConfigurer<>())
			// 手机号验证码登录模拟
			.captchaLogin(captchaLoginConfigurer ->
				// 验证码校验 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				captchaLoginConfigurer
					.captchaService((phone, rawCode) -> {
						return phone.equals(rawCode);
					})
					// 根据手机号查询用户UserDetials  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.captchaUserDetailsService(phone -> {
						return User.withUsername(phone)
							// 密码
							.password("password")
							// 权限集
							.authorities("ROLE_USER", "ROLE_ADMIN").build();
					})
					// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.jwtTokenGenerator(this::tokenResponseMock)
					// 两个登录保持一致
					.successHandler(authenticationSuccessHandler)
					// 两个登录保持一致
					.failureHandler(authenticationFailureHandler)
			)
			// 小程序登录 同时支持多个小程序
			.miniAppLogin(miniAppLoginConfigurer -> miniAppLoginConfigurer
				// 实现小程序多租户
				// 根据请求携带的clientid 查询小程序的appid和secret 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppClientService(this::miniAppClientMock)
				// 小程序用户 自动注册和检索  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppUserDetailsService(new MiniAppUserDetailsServiceMock())
				// 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明   1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppSessionKeyCache(new MiniAppSessionKeyCacheMock())
				// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.jwtTokenGenerator(this::tokenResponseMock)
				// 两个登录保持一致
				.successHandler(authenticationSuccessHandler)
				// 两个登录保持一致
				.failureHandler(authenticationFailureHandler)
			)
			.and()
			.apply(new OAuth2ProviderConfigurer(delegateClientRegistrationRepository))
			// 微信网页授权  下面的参数是假的
			.wechatWebclient("wxdf90xxx8e7f", "bf1306baaaxxxxx15eb02d68df5")
			// 企业微信登录 下面的参数是假的
			.workWechatWebLoginclient("wwa70dc5b6e56936e1", "nvzGI4Alp3xxxxxxZUc3TtPtKbnfTEets5W8",
				"1000005")
			// 微信扫码登录 下面的参数是假的
			.wechatWebLoginclient("wxafd62c05779e50bd", "ab24fce07ea84228dc4e64720f8bdefd")
			.oAuth2LoginConfigurerConsumer(oauth2LoginConfigurer ->
				oauth2LoginConfigurer
					//.loginPage("/user/login").failureUrl("/login-error").permitAll()
					//.loginPage("/login.html").permitAll()
					//.loginProcessingUrl("/login").permitAll()
					//.loginProcessingUrl("/form/login/process").permitAll()
					// 认证成功后的处理器
					// 登录请求url
					// .loginProcessingUrl(DEFAULT_FILTER_PROCESSES_URI)
					.successHandler((request, response, authentication) -> {
						LogUtil.info("用户认证成功");
					})
					// 认证失败后的处理器
					.failureHandler((request, response, exception) -> {
						LogUtil.info("用户认证失败");
					}));
					// // 配置授权服务器端点信息
					// .authorizationEndpoint(authorizationEndpointCustomizer ->
					// 	authorizationEndpointCustomizer
					// 		// 授权端点的前缀基础url
					// 		.baseUri(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
					// )
					// // 配置获取access_token的端点信息
					// .tokenEndpoint(tokenEndpointCustomizer ->
					// 	tokenEndpointCustomizer
					// 		.accessTokenResponseClient(oAuth2AccessTokenResponseClient())
					// )
					// //配置获取userInfo的端点信息
					// .userInfoEndpoint(userInfoEndpointCustomizer ->
					// 	userInfoEndpointCustomizer
					// 		.userService(new QQOauth2UserService())
					// ));

		return http.build();
	}

	JwtAuthenticationConverter jwtAuthenticationConverter() {
		CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}


	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Autowired
	private JwtEncoder jwtEncoder;

	public OAuth2AccessTokenResponse tokenResponseMock(UserDetails userDetails) {
		JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
			.type("JWT")
			.build();

		Instant issuedAt = Clock.system(ZoneId.of("Asia/Shanghai")).instant();
		Set<String> scopes = userDetails.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());

		Instant expiresAt = issuedAt.plusSeconds(5 * 60);
		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
			.issuer("https://felord.cn")
			.subject(userDetails.getUsername())
			.expiresAt(expiresAt)
			.audience(Arrays.asList("client1", "client2"))
			.issuedAt(issuedAt)
			.claim("scope", scopes)
			.build();

		Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet));
		return OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt.getEpochSecond())
			.scopes(scopes)
			.refreshToken(UUID.randomUUID().toString())
			.build();
	}

	private MiniAppClient miniAppClientMock(String clientId) {
		MiniAppClient miniAppClient = new MiniAppClient();
		miniAppClient.setClientId(clientId);
		miniAppClient.setAppId("wx234234324");
		miniAppClient.setSecret("x34431dssf234442231432");
		return miniAppClient;
	}

	static class MiniAppUserDetailsServiceMock implements MiniAppUserDetailsService {

		@Override
		public UserDetails register(MiniAppRequest request) {
			return // 模拟 微信小程序用户注册
				User.withUsername(request.getOpenId())
					// 密码
					.password("password")
					// 权限集
					.authorities("ROLE_USER", "ROLE_ADMIN").build();
		}

		@Override
		public UserDetails loadByOpenId(String clientId, String openId) {
			return // 模拟 根据openid 查询 小程序用户信息
				User.withUsername(openId)
					// 密码
					.password("password")
					// 权限集
					.authorities("ROLE_USER", "ROLE_ADMIN").build();
		}
	}

	static class MiniAppSessionKeyCacheMock implements MiniAppSessionKeyCache {

		@Override
		public String put(String cacheKey, String sessionKey) {
			return sessionKey;
		}

		@Override
		public String get(String cacheKey) {
			// 模拟 sessionkey 缓存
			return "xxxxxxxxxx";
		}
	}
}
