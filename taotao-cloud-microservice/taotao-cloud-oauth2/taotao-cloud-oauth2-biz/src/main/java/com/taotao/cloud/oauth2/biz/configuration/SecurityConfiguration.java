package com.taotao.cloud.oauth2.biz.configuration;

import static com.taotao.cloud.oauth2.biz.configuration.Oauth2ClientConfiguration.oAuth2AccessTokenResponseClient;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
import static org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.oauth2.biz.models.CustomJwtGrantedAuthoritiesConverter;
import com.taotao.cloud.oauth2.biz.service.CloudOauth2UserService;
import com.taotao.cloud.oauth2.biz.service.MemberUserDetailsService;
import com.taotao.cloud.oauth2.biz.service.SysUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;


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
public class SecurityConfiguration {

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

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers(
			"/webjars/**", "/user/login", "/login-error", "/index");
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(
				authorizeRequests -> authorizeRequests
					.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
					//.mvcMatchers("/login.html", "/form/login/process", "/signin.css", "/login", "/login-error").permitAll()
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
						LogUtil.error("认证失败", authException);
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
			.anonymous().disable()
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
}
