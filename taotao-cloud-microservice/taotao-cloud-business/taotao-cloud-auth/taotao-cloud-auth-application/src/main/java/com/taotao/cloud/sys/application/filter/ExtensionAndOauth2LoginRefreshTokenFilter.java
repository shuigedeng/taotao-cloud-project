package com.taotao.cloud.auth.biz.authentication.filter;

import com.taotao.cloud.auth.biz.authentication.token.OAuth2AccessTokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 扩展和oauth2登录刷新令牌过滤器
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OncePerRequestFilter
 * @since 2023-07-12 09:17:53
 */
public class ExtensionAndOauth2LoginRefreshTokenFilter extends OncePerRequestFilter {

	private OAuth2AccessTokenStore oAuth2AccessTokenStore;

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
		new OAuth2AccessTokenResponseHttpMessageConverter();

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
		new AntPathRequestMatcher("/login/token/refresh_token", "POST");

	public ExtensionAndOauth2LoginRefreshTokenFilter(OAuth2AccessTokenStore oAuth2AccessTokenStore) {
		this.oAuth2AccessTokenStore = oAuth2AccessTokenStore;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (!DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		//刷新token 并且返回新token
		String refreshToken = request.getParameter("refresh_token");

		OAuth2AccessTokenResponse accessTokenResponse = oAuth2AccessTokenStore.freshToken(refreshToken);
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

	public OAuth2AccessTokenStore getoAuth2AccessTokenStore() {
		return oAuth2AccessTokenStore;
	}

	public void setoAuth2AccessTokenStore(OAuth2AccessTokenStore oAuth2AccessTokenStore) {
		this.oAuth2AccessTokenStore = oAuth2AccessTokenStore;
	}
}
