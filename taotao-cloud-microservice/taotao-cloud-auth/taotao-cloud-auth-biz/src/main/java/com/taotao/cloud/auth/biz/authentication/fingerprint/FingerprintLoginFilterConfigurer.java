package com.taotao.cloud.auth.biz.authentication.fingerprint;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.fingerprint.service.FingerprintUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class FingerprintLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, FingerprintLoginFilterConfigurer<H>, FingerprintAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private FingerprintUserDetailsService accountUserDetailsService;

	private JwtTokenGenerator jwtTokenGenerator;

	public FingerprintLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new FingerprintAuthenticationFilter(), "/login/captcha");
	}

	public FingerprintLoginFilterConfigurer<H> accountUserDetailsService(FingerprintUserDetailsService accountUserDetailsService) {
		this.accountUserDetailsService = accountUserDetailsService;
		return this;
	}

	public FingerprintLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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
		FingerprintUserDetailsService captchaUserDetailsService =
			this.accountUserDetailsService != null ? this.accountUserDetailsService
				: getBeanOrNull(applicationContext, FingerprintUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		return new FingerprintAuthenticationProvider(accountUserDetailsService);
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