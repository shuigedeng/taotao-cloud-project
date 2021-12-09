package com.taotao.cloud.oauth2.biz.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.taotao.cloud.common.utils.LogUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class Oauth2ClientConfiguration {

	@Autowired
	private OAuth2ClientProperties oAuth2ClientProperties;

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/webjars/**");
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
	}

	private ClientRegistration googleClientRegistration() {
		return ClientRegistration.withRegistrationId("google")
			.clientId("google-client-id")
			.clientSecret("google-client-secret")
			.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
			.scope("openid", "profile", "email", "address", "phone")
			.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
			.tokenUri("https://www.googleapis.com/oauth2/v4/token")
			.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
			.userNameAttributeName(IdTokenClaimNames.SUB)
			.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
			.clientName("Google")
			.build();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			//.authorizeRequests(
			//	authorizeRequests -> authorizeRequests
			//		.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
			//		.anyRequest().authenticated()
			//).formLogin(formLogin -> {
			//		//.loginPage("/auth/login")
			//		//.loginProcessingUrl("/auth/authorize")
			//	}
			//)
			//// 通过httpSession保存认证信息
			//.addFilter(new SecurityContextPersistenceFilter())
			// 配置OAuth2登录认证
			.oauth2Login(
				//oauth2LoginConfigurer -> oauth2LoginConfigurer
				//// 认证成功后的处理器
				//.successHandler((request, response, authentication) -> {
				//	LogUtil.info("用户认证成功");
				//})
				//// 认证失败后的处理器
				//.failureHandler((request, response, exception) -> {
				//	LogUtil.info("用户认证失败");
				//})
				//// 登录请求url
				//.loginProcessingUrl("/api/login/oauth2/code/*")
				//// 配置授权服务器端点信息
				//.authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
				//	// 授权端点的前缀基础url
				//	.baseUri("/api/oauth2/authorization")
				//)
				//// 配置获取access_token的端点信息
				//.tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig
				//	.accessTokenResponseClient(oAuth2AccessTokenResponseClient())
				//)
				// 配置获取userInfo的端点信息
				//.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
				//	.userService(new CloudOauth2UserService())
				//)
			)
			.and()
			//.oauth2Client(withDefaults())
			// 配置匿名用户过滤器
			.csrf().disable()
			.logout()      // (2)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		//.and()
		// 配置认证端点和未授权的请求处理器
		//.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
		//	.authenticationEntryPoint(new CustomizedAuthenticationEntryPoint())
		//	.accessDeniedHandler(new CustomizedAccessDeniedHandler())
		//)
		//.oauth2ResourceServer()
		//.authenticationManagerResolver(customAuthenticationManager())
		//.jwt();

		return http.build();
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
	private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
			new FormHttpMessageConverter(),

			// 解析标准的AccessToken响应信息转换器
			new OAuth2AccessTokenResponseHttpMessageConverter(),

			// 解析qq的AccessToken响应信息转换器
			new QqoAuth2AccessTokenResponseHttpMessageConverter(MediaType.TEXT_HTML)));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		client.setRestOperations(restTemplate);
		return client;
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
			= new RequestMatchingAuthenticationManagerResolver(authenticationManagers);

		// Use opaque tokens (remotely validated) for all other requests
		authenticationManagerResolver.setDefaultAuthenticationManager(opaque());
		return authenticationManagerResolver;
	}

	// Mimic the default configuration for JWT validation.
//	AuthenticationManager jwt() {
//		// this is the keys endpoint for okta
//		String issuer = oAuth2ClientProperties.getProvider().get("okta").getIssuerUri();
//		String jwkSetUri = issuer + "/v1/keys";
//
//		// This is basically the default jwt logic
//		JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
//		authenticationProvider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
//		return authenticationProvider::authenticate;
//	}

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

		public RequestMatchingAuthenticationManagerResolver
			(LinkedHashMap<RequestMatcher, AuthenticationManager> authenticationManagers) {
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
