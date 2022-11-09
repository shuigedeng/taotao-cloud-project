package com.taotao.cloud.auth.biz.configuration;

import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.account.service.AccountUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationService;
import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.face.service.FaceUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.fingerprint.service.FingerprintUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.gestures.service.GesturesUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppClient;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppRequest;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppSessionKeyCache;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppUserInfo;
import com.taotao.cloud.auth.biz.authentication.mp.service.MpUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.oauth2.DelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.oauth2.OAuth2ProviderConfigurer;
import com.taotao.cloud.auth.biz.authentication.oneClick.service.OneClickUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeService;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.models.JwtGrantedAuthoritiesConverter;
import com.taotao.cloud.auth.biz.service.MemberUserDetailsService;
import com.taotao.cloud.auth.biz.service.SysUserDetailsService;
import com.taotao.cloud.auth.biz.utils.RedirectLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.utils.WxUtils;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


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
	mode = AdviceMode.PROXY
)
@EnableWebSecurity
public class SystemSecurityConfiguration implements EnvironmentAware {

	private static Map<String, String> cache = new HashMap<>();

	public static final String[] permitAllUrls = new String[]{
		"/swagger-ui.html",
		"/v3/**",
		"/favicon.ico",
		"/swagger-resources/**",
		"/webjars/**",
		"/actuator/**",
		"/index",
		"/index.html",
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
		"/login/**",
		"/actuator/**",
		"/h2-console/**",
		"/pear.config.json",
		"/pear.config.yml",
		"/admin/css/**",
		"/admin/fonts/**",
		"/admin/js/**",
		"/admin/images/**",
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

	private String exceptionMessage(AuthenticationException exception) {
		String msg = "用户未认证";
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
		LogUtils.error("用户未认证", authException);
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
			// **************************************资源服务器配置***********************************************
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
							.decoder(NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build())
							.jwtAuthenticationConverter(jwtAuthenticationConverter())
					)
			)
			// **************************************自定义登录配置***********************************************
			.apply(new LoginFilterSecurityConfigurer<>())
			// 用户+密码登录
			.accountLogin(accountLoginConfigurer -> {
				accountLoginConfigurer
					.accountUserDetailsService(new AccountUserDetailsService() {
						@Override
						public UserDetails loadUserByPhone(String phone)
							throws UsernameNotFoundException {
							return null;
						}
					});
			})
			//用户+密码+验证码登录
			.accountVerificationLogin(accountVerificationLoginConfigurer -> {
				accountVerificationLoginConfigurer
					.accountVerificationUserDetailsService(
						new AccountVerificationUserDetailsService() {
							@Override
							public UserDetails loadUserByPhone(String phone)
								throws UsernameNotFoundException {
								return null;
							}
						})
					.accountVerificationService(new AccountVerificationService() {
						@Override
						public boolean verifyCaptcha(String verificationCode) {
							return false;
						}
					});
			})
			// 面部识别登录
			.faceLogin(faceLoginLoginConfigurer -> {
				faceLoginLoginConfigurer
					.faceUserDetailsService(new FaceUserDetailsService() {
						@Override
						public UserDetails loadUserByPhone(String phone)
							throws UsernameNotFoundException {

							return null;
						}
					});
			})
			//指纹登录
			.fingerprintLogin(fingerprintLoginConfigurer -> {
				fingerprintLoginConfigurer
					.fingerprintUserDetailsService(
						new FingerprintUserDetailsService() {
							@Override
							public UserDetails loadUserByPhone(String phone)
								throws UsernameNotFoundException {
								return null;
							}
						});
			})
			//手势登录
			.gesturesLogin(fingerprintLoginConfigurer -> {
				fingerprintLoginConfigurer
					.gesturesUserDetailsService(
						new GesturesUserDetailsService() {
							@Override
							public UserDetails loadUserByPhone(String phone)
								throws UsernameNotFoundException {
								return null;
							}
						});
			})
			//本机号码一键登录
			.oneClickLogin(oneClickLoginConfigurer -> {
				oneClickLoginConfigurer
					.oneClickUserDetailsService(new OneClickUserDetailsService() {
						@Override
						public UserDetails loadUserByPhone(String phone)
							throws UsernameNotFoundException {
							return null;
						}
					});
			})
			//手机扫码登录
			.qrcodeLogin(qrcodeLoginConfigurer -> {
				qrcodeLoginConfigurer
					.qrcodeService(new QrcodeService() {
						@Override
						public boolean verifyQrcode(String qrcode) {
							return false;
						}
					})
					.accountUserDetailsService(new QrcodeUserDetailsService() {
						@Override
						public UserDetails loadUserByPhone(String phone)
							throws UsernameNotFoundException {
							return null;
						}
					});
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
					// 两个登录保持一致
					.successHandler(authenticationSuccessHandler)
					// 两个登录保持一致
					.failureHandler(authenticationFailureHandler)
			)
			//微信公众号登录
			.mpLogin(mpLoginConfigurer -> {
				mpLoginConfigurer
					.mpUserDetailsService(new MpUserDetailsService() {
						@Override
						public UserDetails loadUserByPhone(String phone)
							throws UsernameNotFoundException {
							return null;
						}
					});
			})
			// 小程序登录 同时支持多个小程序
			.miniAppLogin(miniAppLoginConfigurer -> miniAppLoginConfigurer
				// 实现小程序多租户
				// 根据请求携带的clientid 查询小程序的appid和secret 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppClientService(clientId -> {
					MiniAppClient miniAppClient = new MiniAppClient();
					miniAppClient.setClientId(clientId);
					miniAppClient.setAppId("wxcd395c35c45eb823");
					miniAppClient.setSecret("75f9a12c82bd24ecac0d37bf1156c749");
					return miniAppClient;
				})
				// 小程序用户 自动注册和检索  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppUserDetailsService(new MiniAppUserDetailsService() {
					@Override
					public UserDetails register(MiniAppRequest request, String sessionKey) {
						System.out.println(request);

						String signature = DigestUtils.sha1Hex(request.getRawData() + sessionKey);
						if (!request.getSignature().equals(signature)) {
							throw new RuntimeException("数字签名验证失败");
						}

						String encryptedData = request.getEncryptedData();
						String iv = request.getIv();

						// 解密encryptedData数据
						String decrypt = WxUtils.decrypt(sessionKey, iv, encryptedData);
						MiniAppUserInfo miniAppUserInfo = JsonUtils.toObject(decrypt,
							MiniAppUserInfo.class);
						miniAppUserInfo.setSessionKey(sessionKey);

						System.out.println(miniAppUserInfo);

						// 调用数据库 微信小程序用户注册

						//模拟
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
					}

					@Override
					public UserDetails loadByOpenId(String clientId, String openId) {
						System.out.println(clientId);
						System.out.println(openId);

						// 模拟 根据openid 查询 小程序用户信息
						return null;
					}
				})
				// 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明   1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				.miniAppSessionKeyCache(new MiniAppSessionKeyCache() {
					@Override
					public String put(String cacheKey, String sessionKey) {
						// 应该小于 微信默认5分钟过期
						cache.put(cacheKey, sessionKey);
						return sessionKey;
					}

					@Override
					public String get(String cacheKey) {
						// 模拟 sessionkey 缓存
						return cache.get(cacheKey);
					}
				})
				// 生成JWT 返回  1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
				// 两个登录保持一致
				.successHandler(authenticationSuccessHandler)
				// 两个登录保持一致
				.failureHandler(authenticationFailureHandler)
			)
			.and()
			// **************************************oauth2登录配置***********************************************
			.apply(new OAuth2ProviderConfigurer(delegateClientRegistrationRepository))
			// 微信网页授权
			.wechatWebclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
			// 企业微信登录
			.workWechatWebLoginclient("wwa70dc5b6e56936e1", "nvzGI4Alp3xxxxxxZUc3TtPtKbnfTEets5W8",
				"1000005")
			// 微信扫码登录
			.wechatWebLoginclient("wxcd395c35c45eb823", "75f9a12c82bd24ecac0d37bf1156c749")
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
						.successHandler(authenticationSuccessHandler)
						// 认证失败后的处理器
						.failureHandler(authenticationFailureHandler)
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
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}


	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}


}
