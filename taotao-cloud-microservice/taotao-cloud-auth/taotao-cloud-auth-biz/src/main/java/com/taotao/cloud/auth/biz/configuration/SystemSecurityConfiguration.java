package com.taotao.cloud.auth.biz.configuration;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppClient;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppClientService;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppRequest;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppSessionKeyCache;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.oauth2.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.oauth2.OAuth2ProviderConfigurer;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeService;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeUserDetailsService;
import com.taotao.cloud.auth.biz.models.CustomJwtGrantedAuthoritiesConverter;
import com.taotao.cloud.auth.biz.service.MemberUserDetailsService;
import com.taotao.cloud.auth.biz.service.SysUserDetailsService;
import com.taotao.cloud.auth.biz.utils.RedirectLoginAuthenticationSuccessHandler;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
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
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


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
public class SystemSecurityConfiguration implements EnvironmentAware {

	public static final String[] permitAllUrls = new String[]{
		"/swagger-ui.html",
		"/v3/**",
		"/favicon.ico",
		"/swagger-resources/**",
		"/webjars/**",
		"/actuator/**",
		"/index",
		"/index.html",
		// "/auth/captcha/code",
		"/auth/qrcode/code",
		"/auth/sms/phone",
		"/doc.html",
		"/*.js",
		"/*.css",
		"/*.json",
		"/*.min.js",
		"/*.min.css",
		"/**.js",
		"/**.css",
		"/**.json",
		"/**.min.js",
		"/**.min.css",
		"/component/**",
		"/actuator/health",
		"/h2-console/**",
		"/pear.config.json",
		"/pear.config.yml",
		"/admin/css/**",
		"/admin/fonts/**",
		"/admin/js/**",
		"/admin/images/**",
		"/api/test/**",
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

	//@Autowired
	//private JwtTokenGenerator jwtTokenGenerator;

	AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
		LogUtils.error("用户认证失败", authException);
		authException.printStackTrace();
		ResponseUtils.fail(response, exceptionMessage(authException));
	};
	AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		LogUtils.error("用户权限不足", accessDeniedException);
		ResponseUtils.fail(response, ResultEnum.FORBIDDEN);
	};
	AuthenticationFailureHandler authenticationFailureHandler = (request, response, accessDeniedException) -> {
		LogUtils.error("账号或者密码错误", accessDeniedException);
		ResponseUtils.fail(response, "账号或者密码错误");
	};
	AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
		LogUtils.error("用户认证成功", authentication);
		ResponseUtils.success(response,
			tokenResponse((UserDetails) authentication.getPrincipal()));
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
					.loginProcessingUrl("/login")
					.successHandler(new RedirectLoginAuthenticationSuccessHandler())
					.failureHandler(authenticationFailureHandler).permitAll()
					.usernameParameter("username")
					.passwordParameter("password");
			})
			.userDetailsService(new MemberUserDetailsService())
			.anonymous()
			.and()
			.csrf()
			.disable()
			.logout()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.and()
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
					.antMatchers(permitAllUrls).permitAll()
					.antMatchers("/webjars/**", "/user/login", "/login-error", "/index").permitAll()
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
			// 用户+密码登录
			.accountLogin(accountLoginConfigurer -> {

			})
			//用户+密码+验证码登录
			.accountVerificationLogin(accountVerificationLoginConfigurer -> {

			})
			// 面部识别登录
			.faceLogin(faceLoginLoginConfigurer -> {

			})
			//指纹登录
			.fingerprintLogin(fingerprintLoginConfigurer -> {

			})
			//手势登录
			.gesturesLogin(fingerprintLoginConfigurer -> {

			})
			//本机号码一键登录
			.oneClickLogin(oneClickLoginConfigurer -> {

			})
			//手机扫码登录
			.qrcodeLogin(qrcodeLoginConfigurer -> {
				qrcodeLoginConfigurer.qrcodeService(new QrcodeService() {
					@Override
					public boolean verifyQrcode(String qrcode) {
						return false;
					}
				}).accountUserDetailsService(new QrcodeUserDetailsService() {
					@Override
					public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
						return null;
					}
				}).jwtTokenGenerator(this::tokenResponse);
			})
			// 手机号码+短信登录
			.phoneLogin(phoneLoginConfigurer ->
				// 验证码校验 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				phoneLoginConfigurer
					.phoneService((phone, rawCode) -> {
						System.out.println(phone);
						System.out.println(rawCode);

						return true;
					})
					// 根据手机号查询用户UserDetials  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.phoneUserDetailsService(phone -> {
						return SecurityUser.builder()
							.account("admin")
							.userId(1L)
							.username("admin")
							.nickname("admin")
							.password(
								"$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
							.phone("15730445331")
							.mobile("15730445331")
							.email("981376578@qq.com")
							.sex(1)
							.status(1)
							.type(2)
							.permissions(Set.of("xxx", "sldfl"))
							.build();
					})
					// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
					.jwtTokenGenerator(this::tokenResponse)
					// 两个登录保持一致
					.successHandler(authenticationSuccessHandler)
					// 两个登录保持一致
					.failureHandler(authenticationFailureHandler)
			)
			//微信公众号登录
			.mpLogin(mpLoginConfigurer -> {

			})
			// 小程序登录 同时支持多个小程序
			.miniAppLogin(miniAppLoginConfigurer -> miniAppLoginConfigurer
				// 实现小程序多租户
				// 根据请求携带的clientid 查询小程序的appid和secret 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppClientService(new MiniAppClientService() {
					@Override
					public MiniAppClient get(String clientId) {
						MiniAppClient miniAppClient = new MiniAppClient();
						miniAppClient.setClientId(clientId);
						miniAppClient.setAppId("wx234234324");
						miniAppClient.setSecret("x34431dssf234442231432");
						return miniAppClient;
					}
				})
				// 小程序用户 自动注册和检索  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppUserDetailsService(new MiniAppUserDetailsService() {
					@Override
					public UserDetails register(MiniAppRequest request) {
						return // 模拟 微信小程序用户注册
							User.withUsername(request.getOpenId())
								.password("password")
								.authorities("ROLE_USER", "ROLE_ADMIN").build();
					}

					@Override
					public UserDetails loadByOpenId(String clientId, String openId) {
						return // 模拟 根据openid 查询 小程序用户信息
							User.withUsername(openId)
								.password("password")
								.authorities("ROLE_USER", "ROLE_ADMIN").build();
					}
				})
				// 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明   1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppSessionKeyCache(new MiniAppSessionKeyCache() {
					@Override
					public String put(String cacheKey, String sessionKey) {
						return sessionKey;
					}

					@Override
					public String get(String cacheKey) {
						// 模拟 sessionkey 缓存
						return "xxxxxxxxxx";
					}
				})
				// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.jwtTokenGenerator(this::tokenResponse)
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
						//.loginPage("/login")
						.successHandler((request, response, authentication) -> {
							LogUtils.info("oAuth2Login用户认证成功");
						})
						// 认证失败后的处理器
						.failureHandler((request, response, exception) -> {
							LogUtils.info("oAuth2Login用户认证失败");
						})
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

			);
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

	public OAuth2AccessTokenResponse tokenResponse(UserDetails userDetails) {
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

		JWKSource jwkSource = ContextUtils.getBean(JWKSource.class, false);
		Jwt jwt = new NimbusJwtEncoder(jwkSource).encode(
			JwtEncoderParameters.from(jwsHeader, claimsSet));
		return OAuth2AccessTokenResponse.withToken(jwt.getTokenValue())
			.tokenType(OAuth2AccessToken.TokenType.BEARER)
			.expiresIn(expiresAt.getEpochSecond())
			.scopes(scopes)
			.refreshToken(UUID.randomUUID().toString())
			.build();
	}


}
