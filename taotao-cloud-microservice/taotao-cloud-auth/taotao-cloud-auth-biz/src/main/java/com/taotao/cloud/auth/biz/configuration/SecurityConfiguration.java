package com.taotao.cloud.auth.biz.configuration;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
import static org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

import com.taotao.cloud.auth.biz.models.CustomJwtGrantedAuthoritiesConverter;
import com.taotao.cloud.auth.biz.service.CloudOauth2UserService;
import com.taotao.cloud.auth.biz.service.MemberUserDetailsService;
import com.taotao.cloud.auth.biz.service.SysUserDetailsService;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
import org.springframework.security.web.SecurityFilterChain;
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
public class SecurityConfiguration implements EnvironmentAware {

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

	@Autowired
	private OAuth2ClientProperties oAuth2ClientProperties;

	@Value("${jwk.set.uri}")
	private String jwkSetUri;

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

	@Autowired(required = false)
	private DiscoveryClient discoveryClient;

	private Environment environment;

	@Bean
	public JwtDecoder jwtDecoder() {
		if (Stream.of(environment.getActiveProfiles()).toList().contains("dev")) {
			if (Objects.nonNull(discoveryClient)) {
				jwkSetUri = discoveryClient.getServices().stream()
					.filter(s -> s.contains(ServiceName.TAOTAO_CLOUD_AUTH))
					.flatMap(s -> discoveryClient.getInstances(s).stream())
					.map(instance -> String.format("http://%s:%s" + "/oauth2/jwks",
						instance.getHost(),
						instance.getPort()))
					.findFirst()
					.orElse(jwkSetUri);
			}
		}

		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.antMatchers(permitAllUrls)
			.antMatchers("/webjars/**", "/user/login", "/login-error", "/index");
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(
				authorizeRequests -> authorizeRequests
					.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
					.antMatchers(permitAllUrls).permitAll()
					.mvcMatchers("/user/login", "/login-error", "/index").permitAll()
					.mvcMatchers("/messages/**").access("hasAuthority('ADMIN')")
					.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth2ResourceServerCustomizer ->
				oauth2ResourceServerCustomizer
					.accessDeniedHandler((request, response, accessDeniedException) -> {
						LogUtil.error("用户权限不足", accessDeniedException);
						ResponseUtil.fail(response, ResultEnum.FORBIDDEN);
					})
					.authenticationEntryPoint((request, response, authException) -> {
						LogUtil.error("用户认证失败", authException);
						authException.printStackTrace();
						ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
					})
					.bearerTokenResolver(request -> {
						DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
						defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
						defaultBearerTokenResolver.setAllowUriQueryParameter(true);
						return defaultBearerTokenResolver.resolve(request);
					})
					.jwt(jwt -> jwt.decoder(jwtDecoder())
						.jwtAuthenticationConverter(jwtAuthenticationConverter()))
			)
			.oauth2Login(oauth2LoginConfigurer ->
				oauth2LoginConfigurer
					//.loginPage("/user/login").failureUrl("/login-error").permitAll()

					//.loginPage("/login.html").permitAll()
					//.loginProcessingUrl("/login").permitAll()
					//.loginProcessingUrl("/form/login/process").permitAll()
					// 认证成功后的处理器
					.successHandler((request, response, authentication) -> {
						LogUtil.info("用户认证成功");
					})
					// 认证失败后的处理器
					.failureHandler((request, response, exception) -> {
						LogUtil.info("用户认证失败");
					})
					// 登录请求url
					.loginProcessingUrl(DEFAULT_FILTER_PROCESSES_URI)
					// 配置授权服务器端点信息
					.authorizationEndpoint(authorizationEndpointCustomizer ->
						authorizationEndpointCustomizer
							// 授权端点的前缀基础url
							.baseUri(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
					)
					// 配置获取access_token的端点信息
					.tokenEndpoint(tokenEndpointCustomizer ->
						tokenEndpointCustomizer
							.accessTokenResponseClient(oAuth2AccessTokenResponseClient())
					)
					//配置获取userInfo的端点信息
					.userInfoEndpoint(userInfoEndpointCustomizer ->
						userInfoEndpointCustomizer
							.userService(new CloudOauth2UserService())
					)
			)
			.formLogin(withDefaults())
			//.formLogin(formLoginCustomizer -> {
			//	formLoginCustomizer
			//		.loginPage("/login.html").permitAll()
			//		.loginProcessingUrl("/login").permitAll()
			//		//.failureForwardUrl("/form/login").permitAll()
			//		//.successForwardUrl("/form/login/success").permitAll()
			//		.usernameParameter("username")
			//		.passwordParameter("password");
			//})
			//.formLogin(form -> form.loginPage("/user/login").failureUrl("/login-error").permitAll())
			//.exceptionHandling(exceptionHandlingCustomizer-> {
			//	exceptionHandlingCustomizer
			//		.accessDeniedHandler((request, response, accessDeniedException) -> {
			//			LogUtil.error("用户权限不足********", accessDeniedException);
			//			ResponseUtil.fail(response, ResultEnum.FORBIDDEN);
			//		})
			//		.authenticationEntryPoint((request, response, authException) -> {
			//			LogUtil.error("认证失败111111111111", authException);
			//			authException.printStackTrace();
			//			ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
			//		});
			//})
			.anonymous()
			.and()
			.csrf().disable()
			.logout()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

		return http.build();
	}

	JwtAuthenticationConverter jwtAuthenticationConverter() {
		CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	/**
	 * qq获取access_token返回的结果是类似get请求参数的字符串，无法通过指定Accept请求头来使qq返回特定的响应类型，并且qq返回的access_token
	 * 也缺少了必须的token_type字段（不符合oauth2标准的授权码认证流程），spring-security默认远程获取 access_token的客户端是{@link
	 * DefaultAuthorizationCodeTokenResponseClient}，所以我们需要 自定义{@link QqoAuth2AccessTokenResponseHttpMessageConverter}注入到这个client中来解析qq的access_token响应信息
	 *
	 * @return {@link DefaultAuthorizationCodeTokenResponseClient} 用来获取access_token的客户端
	 * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/authorization-code-request">authorization-code-request规范</a>
	 * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-response">access-token-response规范</a>
	 * @see <a href="https://wiki.connect.qq.com/%E5%BC%80%E5%8F%91%E6%94%BB%E7%95%A5_server-side">qq开发文档</a>
	 */
	public static OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
			new FormHttpMessageConverter(),

			// 解析标准的AccessToken响应信息转换器
			new OAuth2AccessTokenResponseHttpMessageConverter(),

			// 解析qq的AccessToken响应信息转换器
			new QqoAuth2AccessTokenResponseHttpMessageConverter(
				MediaType.TEXT_HTML)));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		client.setRestOperations(restTemplate);
		return client;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * 自定义消息转换器来解析qq的access_token响应信息
	 *
	 * @see OAuth2AccessTokenResponseHttpMessageConverter#readInternal(Class, HttpInputMessage)
	 */
	private static class QqoAuth2AccessTokenResponseHttpMessageConverter extends
		OAuth2AccessTokenResponseHttpMessageConverter {

		public QqoAuth2AccessTokenResponseHttpMessageConverter(MediaType... mediaType) {
			setSupportedMediaTypes(Arrays.asList(mediaType));
		}

		@Override
		protected OAuth2AccessTokenResponse readInternal(
			Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage) {

			String response = null;
			try {
				response = StreamUtils
					.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			LogUtil.info("qq的AccessToken响应信息：{}", response);

			// 解析响应信息类似access_token=YOUR_ACCESS_TOKEN&expires_in=3600这样的字符串
			Map<String, String> tokenResponseParameters = Arrays.stream(response.split("&"))
				.collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));

			// 手动给qq的access_token响应信息添加token_type字段，spring-security会按照oauth2规范校验返回参数
			tokenResponseParameters.put(OAuth2ParameterNames.TOKEN_TYPE, "bearer");
			return this.tokenResponseConverter.convert(tokenResponseParameters);
		}

		@Override
		protected void writeInternal(OAuth2AccessTokenResponse tokenResponse,
			HttpOutputMessage outputMessage) {
			throw new UnsupportedOperationException();
		}
	}


	AuthenticationManagerResolver<HttpServletRequest> customAuthenticationManager() {
		LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers = new LinkedHashMap<>();

		// USE JWT tokens (locally validated) to validate HEAD, GET, and OPTIONS requests
		List<String> readMethod = Arrays.asList("HEAD", "GET", "OPTIONS");
		RequestMatcher readMethodRequestMatcher = request -> readMethod
			.contains(request.getMethod());
		authenticationManagers.put(readMethodRequestMatcher, jwt());

		// all other requests will use opaque tokens (remotely validated)
		RequestMatchingAuthenticationManagerResolver authenticationManagerResolver
			= new RequestMatchingAuthenticationManagerResolver(
			authenticationManagers);

		// Use opaque tokens (remotely validated) for all other requests
		authenticationManagerResolver.setDefaultAuthenticationManager(opaque());
		return authenticationManagerResolver;
	}

	// Mimic the default configuration for JWT validation.
	AuthenticationManager defaultJwt() {
		// this is the keys endpoint for okta
		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
		String jwkSetUri = issuer + "/v1/keys";

		// This is basically the default jwt logic
		JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(
			jwtDecoder);
		authenticationProvider.setJwtAuthenticationConverter(
			new JwtBearerTokenAuthenticationConverter());
		return authenticationProvider::authenticate;
	}

	AuthenticationManager jwt() {
		// this is the keys endpoint for okta
		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
		String jwkSetUri = issuer + "/v1/keys";

		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

		// okta recommends validating the `iss` and `aud` claims
		// see: https://developer.okta.com/docs/guides/validate-access-tokens/java/overview/
		List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
		validators.add(new JwtTimestampValidator());
		// Add validation of the issuer claim
		validators.add(new JwtIssuerValidator(issuer));
		validators.add(token -> {
			Set<String> expectedAudience = new HashSet<>();
			// Add validation of the audience claim
			expectedAudience.add("api://default");
			// For new Okta orgs, the default audience is `api://default`,
			// if you have changed this from the default update this value
			return !Collections.disjoint(token.getAudience(), expectedAudience)
				? OAuth2TokenValidatorResult.success()
				: OAuth2TokenValidatorResult.failure(new OAuth2Error(
					OAuth2ErrorCodes.INVALID_REQUEST,
					"This aud claim is not equal to the configured audience",
					"https://tools.ietf.org/html/rfc6750#section-3.1"));
		});

		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(validators);
		jwtDecoder.setJwtValidator(validator);

		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(
			jwtDecoder);
		authenticationProvider
			.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
		return authenticationProvider::authenticate;
	}

	// Mimic the default configuration for opaque token validation
	AuthenticationManager opaque() {
		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
		String introspectionUri = issuer + "/v1/introspect";

		// The default opaque token logic
		OAuth2ClientProperties.Registration oktaRegistration = oAuth2ClientProperties
			.getRegistration().get("okta");
		OpaqueTokenIntrospector introspectionClient = new NimbusOpaqueTokenIntrospector(
			introspectionUri,
			oktaRegistration.getClientId(),
			oktaRegistration.getClientSecret());
		return new OpaqueTokenAuthenticationProvider(introspectionClient)::authenticate;
	}


	public static class RequestMatchingAuthenticationManagerResolver implements
		AuthenticationManagerResolver<HttpServletRequest> {

		private final LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers;

		private AuthenticationManager defaultAuthenticationManager = authentication -> {
			throw new AuthenticationServiceException("Cannot authenticate " + authentication);
		};

		public RequestMatchingAuthenticationManagerResolver(
			LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers) {
			Assert.notEmpty(authenticationManagers, "authenticationManagers cannot be empty");
			this.authenticationManagers = authenticationManagers;
		}

		@Override
		public AuthenticationManager resolve(HttpServletRequest context) {
			for (Map.Entry<RequestMatcher, AuthenticationManager> entry : this.authenticationManagers
				.entrySet()) {
				if (entry.getKey().matches(context)) {
					return entry.getValue();
				}
			}

			return this.defaultAuthenticationManager;
		}

		public void setDefaultAuthenticationManager(
			AuthenticationManager defaultAuthenticationManager) {
			Assert.notNull(defaultAuthenticationManager,
				"defaultAuthenticationManager cannot be null");
			this.defaultAuthenticationManager = defaultAuthenticationManager;
		}
	}
}
