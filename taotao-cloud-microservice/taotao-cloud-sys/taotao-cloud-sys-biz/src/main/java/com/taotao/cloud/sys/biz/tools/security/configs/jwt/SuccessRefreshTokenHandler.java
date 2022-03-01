package com.taotao.cloud.sys.biz.tools.security.configs.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 登录成功时刷新 token
 */
@Component
public class SuccessRefreshTokenHandler implements AuthenticationSuccessHandler {
	@Autowired
	private TokenService tokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		final Claims claims = jwtAuthenticationToken.getClaims();

		final Date issuedAt = claims.getIssuedAt();
		LocalDateTime issueTime = LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneId.systemDefault());
		final boolean needRefresh = LocalDateTime.now().minusSeconds(TokenService.issueAtSecond).isAfter(issueTime);
		if (needRefresh){
			final String username = claims.get("username", String.class);
			final ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
			String tokenNew = tokenService.generatorToken(new TokenService.TokenInfo(username));
			response.setHeader("Authorization", tokenNew);
		}
	}
}
