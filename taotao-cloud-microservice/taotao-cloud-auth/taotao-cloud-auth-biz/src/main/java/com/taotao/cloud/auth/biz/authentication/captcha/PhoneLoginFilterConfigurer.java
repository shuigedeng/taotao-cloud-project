package com.taotao.cloud.auth.biz.authentication.captcha;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.captcha.service.PhoneService;
import com.taotao.cloud.auth.biz.authentication.captcha.service.PhoneUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class PhoneLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, PhoneLoginFilterConfigurer<H>, PhoneAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private PhoneUserDetailsService captchaUserDetailsService;

	private PhoneService captchaService;

	private JwtTokenGenerator jwtTokenGenerator;

	public PhoneLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new PhoneAuthenticationFilter(), "/login/captcha");
	}

	public PhoneLoginFilterConfigurer<H> captchaUserDetailsService(
		PhoneUserDetailsService captchaUserDetailsService) {
		this.captchaUserDetailsService = captchaUserDetailsService;
		return this;
	}

	public PhoneLoginFilterConfigurer<H> captchaService(PhoneService captchaService) {
		this.captchaService = captchaService;
		return this;
	}

	public PhoneLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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
		PhoneUserDetailsService captchaUserDetailsService =
			this.captchaUserDetailsService != null ? this.captchaUserDetailsService
				: getBeanOrNull(applicationContext, PhoneUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		PhoneService captchaService = this.captchaService != null ? this.captchaService
			: getBeanOrNull(applicationContext, PhoneService.class);
		Assert.notNull(captchaService, "captchaService is required");
		return new PhoneAuthenticationProvider(captchaUserDetailsService, captchaService);
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
