package com.taotao.cloud.auth.biz.authentication.gestures;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.gestures.service.GesturesUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class GesturesLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, GesturesLoginFilterConfigurer<H>, GesturesAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private GesturesUserDetailsService accountUserDetailsService;

	private JwtTokenGenerator jwtTokenGenerator;

	public GesturesLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new GesturesAuthenticationFilter(), "/login/captcha");
	}

	public GesturesLoginFilterConfigurer<H> accountUserDetailsService(GesturesUserDetailsService accountUserDetailsService) {
		this.accountUserDetailsService = accountUserDetailsService;
		return this;
	}

	public GesturesLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
		this.jwtTokenGenerator = jwtTokenGenerator;
		return this;
	}

	@Override
	protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
		return new AntPathRequestMatcher(loginProcessingUrl, "POST");
	}

	@Override
	protected AuthenticationProvider authenticationProvider(H http) {
		ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
		GesturesUserDetailsService captchaUserDetailsService =
			this.accountUserDetailsService != null ? this.accountUserDetailsService
				: getBeanOrNull(applicationContext, GesturesUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		return new GesturesAuthenticationProvider(accountUserDetailsService);
	}

	@Override
	protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
		if (this.jwtTokenGenerator == null) {
			ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
			jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
		}
		Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
		//return new LoginAuthenticationSuccessHandler(jwtTokenGenerator);
		return null;
	}
}
