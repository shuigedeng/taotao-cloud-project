package com.taotao.cloud.auth.biz.authentication.accountVerification;

import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationService;
import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationUserDetailsService;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.models.LoginAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class AccountVerificationLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, AccountVerificationLoginFilterConfigurer<H>, AccountVerificationAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private AccountVerificationUserDetailsService accountVerificationUserDetailsService;

	private AccountVerificationService accountVerificationService;

	private JwtTokenGenerator jwtTokenGenerator;

	public AccountVerificationLoginFilterConfigurer(
		LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new AccountVerificationAuthenticationFilter(),
			"/login/account/verification");
	}

	public AccountVerificationLoginFilterConfigurer<H> accountVerificationUserDetailsService(
		AccountVerificationUserDetailsService accountVerificationUserDetailsService) {
		this.accountVerificationUserDetailsService = accountVerificationUserDetailsService;
		return this;
	}

	public AccountVerificationLoginFilterConfigurer<H> accountVerificationService(
		AccountVerificationService accountVerificationService) {
		this.accountVerificationService = accountVerificationService;
		return this;
	}

	public AccountVerificationLoginFilterConfigurer<H> jwtTokenGenerator(
		JwtTokenGenerator jwtTokenGenerator) {
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
		AccountVerificationUserDetailsService captchaUserDetailsService =
			this.accountVerificationUserDetailsService != null
				? this.accountVerificationUserDetailsService
				: getBeanOrNull(applicationContext, AccountVerificationUserDetailsService.class);
		Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService is required");
		AccountVerificationService captchaService =
			this.accountVerificationService != null ? this.accountVerificationService
				: getBeanOrNull(applicationContext, AccountVerificationService.class);
		Assert.notNull(captchaService, "captchaService is required");
		return new AccountVerificationAuthenticationProvider(captchaUserDetailsService,
			captchaService);
	}

	@Override
	protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
		if (this.jwtTokenGenerator == null) {
			ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
			jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
		}
		Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
		return new LoginAuthenticationSuccessHandler(jwtTokenGenerator);
	}
}
