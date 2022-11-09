package com.taotao.cloud.auth.biz.authentication.miniapp;


import com.taotao.cloud.auth.biz.authentication.AbstractLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.LoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.jwt.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.models.LoginAuthenticationSuccessHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class MiniAppLoginFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
	AbstractLoginFilterConfigurer<H, MiniAppLoginFilterConfigurer<H>, MiniAppAuthenticationFilter, LoginFilterSecurityConfigurer<H>> {

	private MiniAppUserDetailsService miniAppUserDetailsService;

	private JwtTokenGenerator jwtTokenGenerator;

	private MiniAppClientService miniAppClientService;

	private MiniAppSessionKeyCache miniAppSessionKeyCache;

	public MiniAppLoginFilterConfigurer(LoginFilterSecurityConfigurer<H> securityConfigurer) {
		super(securityConfigurer, new MiniAppAuthenticationFilter(), "/login/miniapp");
	}

	public MiniAppLoginFilterConfigurer<H> miniAppUserDetailsService(
		MiniAppUserDetailsService miniAppUserDetailsService) {
		this.miniAppUserDetailsService = miniAppUserDetailsService;
		return this;
	}

	public MiniAppLoginFilterConfigurer<H> miniAppClientService(
		MiniAppClientService miniAppClientService) {
		this.miniAppClientService = miniAppClientService;
		return this;
	}

	public MiniAppLoginFilterConfigurer<H> miniAppSessionKeyCache(
		MiniAppSessionKeyCache miniAppSessionKeyCache) {
		this.miniAppSessionKeyCache = miniAppSessionKeyCache;
		return this;
	}

	public MiniAppLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
		this.jwtTokenGenerator = jwtTokenGenerator;
		return this;
	}

	@Override
	public void configure(H http) throws Exception {
		super.configure(http);
		initPreAuthenticationFilter(http);
	}

	private void initPreAuthenticationFilter(H http) {
		ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
		MiniAppClientService miniAppClientService =
			this.miniAppClientService != null ? this.miniAppClientService
				: getBeanOrNull(applicationContext, MiniAppClientService.class);
		MiniAppSessionKeyCache miniAppSessionKeyCache =
			this.miniAppSessionKeyCache != null ? this.miniAppSessionKeyCache
				: getBeanOrNull(applicationContext, MiniAppSessionKeyCache.class);

		MiniAppPreAuthenticationFilter miniAppPreAuthenticationFilter = new MiniAppPreAuthenticationFilter(
			miniAppClientService, miniAppSessionKeyCache);
		http.addFilterBefore(postProcess(miniAppPreAuthenticationFilter), LogoutFilter.class);
	}

	@Override
	protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
		return new AntPathRequestMatcher(loginProcessingUrl, "POST");
	}

	@Override
	protected AuthenticationProvider authenticationProvider(H http) {
		ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
		MiniAppUserDetailsService miniAppUserDetailsService =
			this.miniAppUserDetailsService != null ? this.miniAppUserDetailsService
				: getBeanOrNull(applicationContext, MiniAppUserDetailsService.class);

		MiniAppSessionKeyCache miniAppSessionKeyCache =
			this.miniAppSessionKeyCache != null ? this.miniAppSessionKeyCache
				: getBeanOrNull(applicationContext, MiniAppSessionKeyCache.class);

		return new MiniAppAuthenticationProvider(miniAppUserDetailsService, miniAppSessionKeyCache);
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
