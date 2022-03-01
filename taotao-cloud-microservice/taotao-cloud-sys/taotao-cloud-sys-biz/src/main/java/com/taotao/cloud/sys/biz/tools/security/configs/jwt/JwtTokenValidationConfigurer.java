package com.taotao.cloud.sys.biz.tools.security.configs.jwt;

import com.taotao.cloud.sys.biz.tools.security.configs.jsonlogin.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtTokenValidationConfigurer<T extends JwtTokenValidationConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    @Autowired
	private SuccessRefreshTokenHandler successRefreshTokenHandler;
    @Autowired
	private ResponseHandler responseHandler;

	@Override
	public void configure(B http) throws Exception {
		final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
		jwtAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

		jwtAuthenticationFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
				log.error("走的这里?");
				responseHandler.writeAuthenticationFail(exception,response);
			}
		});
		jwtAuthenticationFilter.setAuthenticationSuccessHandler(successRefreshTokenHandler);

		http.addFilterAfter(postProcess(jwtAuthenticationFilter), LogoutFilter.class);
	}

}
