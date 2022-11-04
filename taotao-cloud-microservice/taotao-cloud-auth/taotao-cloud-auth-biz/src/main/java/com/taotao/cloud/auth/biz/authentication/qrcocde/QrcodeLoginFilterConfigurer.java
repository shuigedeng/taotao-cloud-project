package com.taotao.cloud.auth.biz.authentication.qrcocde;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeService;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class QrcodeLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, QrcodeLoginFilterConfigurer<H>, QrcodeAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private QrcodeUserDetailsService accountUserDetailsService;
	private QrcodeService qrcodeService;
	private JwtTokenGenerator jwtTokenGenerator;

	public QrcodeLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new QrcodeAuthenticationFilter(), "/login/captcha");
	}

	public QrcodeLoginFilterConfigurer<H> accountUserDetailsService(QrcodeUserDetailsService accountUserDetailsService) {
		this.accountUserDetailsService = accountUserDetailsService;
		return this;
	}

	public QrcodeLoginFilterConfigurer<H> qrcodeService(QrcodeService qrcodeService) {
		this.qrcodeService = qrcodeService;
		return this;
	}

	public QrcodeLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
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
		QrcodeUserDetailsService captchaUserDetailsService =
			this.accountUserDetailsService != null ? this.accountUserDetailsService
				: getBeanOrNull(applicationContext, QrcodeUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		QrcodeService qrcodeService =
			this.qrcodeService != null ? this.qrcodeService
				: getBeanOrNull(applicationContext, QrcodeService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		return new QrcodeAuthenticationProvider(qrcodeService, accountUserDetailsService);
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
