package com.taotao.cloud.auth.biz.authentication.qrcocde;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class QrcodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_UUID_KEY = "uuid";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
			"/login/qrcode", "POST");

	private String uuidParameter = SPRING_SECURITY_FORM_UUID_KEY;

	private Converter<HttpServletRequest, QrcodeAuthenticationToken> qrcodeAuthenticationTokenConverter;

	private boolean postOnly = true;

	public QrcodeAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.qrcodeAuthenticationTokenConverter = defaultConverter();
	}

	public QrcodeAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.qrcodeAuthenticationTokenConverter = defaultConverter();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}

		QrcodeAuthenticationToken authRequest = qrcodeAuthenticationTokenConverter.convert(request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, QrcodeAuthenticationToken> defaultConverter() {
		return request -> {
			String username = request.getParameter(this.uuidParameter);
			username = (username != null) ? username.trim() : "";

			String authorization = request.getHeader("Authorization");
			if (StrUtil.isBlank(authorization)) {
				throw new AuthenticationCredentialsNotFoundException("");
			}

			return new QrcodeAuthenticationToken(username, "");
		};
	}


	protected void setDetails(HttpServletRequest request, QrcodeAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		// this.usernameParameter = usernameParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		// this.passwordParameter = passwordParameter;
	}

	public void setConverter(Converter<HttpServletRequest, QrcodeAuthenticationToken> converter) {
		Assert.notNull(converter, "Converter must not be null");
		this.qrcodeAuthenticationTokenConverter = converter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

}
