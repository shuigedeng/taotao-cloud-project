package com.taotao.cloud.auth.biz.authentication.captcha;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.captcha.service.CaptchaService;
import com.taotao.cloud.auth.biz.authentication.captcha.service.CaptchaUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class CaptchaLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, CaptchaLoginFilterConfigurer<H>, CaptchaAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private CaptchaUserDetailsService captchaUserDetailsService;

	private CaptchaService captchaService;

	private JwtTokenGenerator jwtTokenGenerator;

	public CaptchaLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new CaptchaAuthenticationFilter(), "/login/captcha");
	}

	public CaptchaLoginFilterConfigurer<H> captchaUserDetailsService(CaptchaUserDetailsService captchaUserDetailsService) {
		this.captchaUserDetailsService = captchaUserDetailsService;
		return this;
	}

	public CaptchaLoginFilterConfigurer<H> captchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
		return this;
	}

	public CaptchaLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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
		CaptchaUserDetailsService captchaUserDetailsService = this.captchaUserDetailsService != null ? this.captchaUserDetailsService : getBeanOrNull(applicationContext, CaptchaUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		CaptchaService captchaService = this.captchaService != null ? this.captchaService : getBeanOrNull(applicationContext, CaptchaService.class);
		Assert.notNull(captchaService, "captchaService is required");
		return new CaptchaAuthenticationProvider(captchaUserDetailsService, captchaService);
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
