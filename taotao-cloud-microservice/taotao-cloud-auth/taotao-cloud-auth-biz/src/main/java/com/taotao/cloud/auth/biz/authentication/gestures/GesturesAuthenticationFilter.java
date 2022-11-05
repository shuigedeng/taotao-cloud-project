package com.taotao.cloud.auth.biz.authentication.gestures;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class GesturesAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/login/gestures", "POST");

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	private Converter<HttpServletRequest, GesturesAuthenticationToken> gesturesAuthenticationTokenConverter;

	private boolean postOnly = true;

	public GesturesAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.gesturesAuthenticationTokenConverter = defaultConverter();
	}

	public GesturesAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.gesturesAuthenticationTokenConverter = defaultConverter();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		GesturesAuthenticationToken authRequest = gesturesAuthenticationTokenConverter.convert(
			request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, GesturesAuthenticationToken> defaultConverter() {
		return request -> {
			String username = request.getParameter(this.usernameParameter);
			username = (username != null) ? username.trim() : "";

			String passord = request.getParameter(this.passwordParameter);
			passord = (passord != null) ? passord.trim() : "";

			return new GesturesAuthenticationToken(username, passord);
		};
	}


	protected void setDetails(HttpServletRequest request, GesturesAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public void setConverter(Converter<HttpServletRequest, GesturesAuthenticationToken> converter) {
		Assert.notNull(converter, "Converter must not be null");
		this.gesturesAuthenticationTokenConverter = converter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getUsernameParameter() {
		return this.usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

}
