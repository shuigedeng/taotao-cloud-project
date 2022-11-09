package com.taotao.cloud.auth.biz.authentication.account;

import com.taotao.cloud.common.enums.LoginTypeEnum;
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

public class AccountAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/login/account", "POST");

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	/**
	 * @see LoginTypeEnum B_PC_ACCOUNT / C_PC_ACCOUNT
	 */
	private String typeParameter = SPRING_SECURITY_FORM_TYPE_KEY;

	private Converter<HttpServletRequest, AccountAuthenticationToken> accountVerificationAuthenticationTokenConverter;

	private boolean postOnly = true;

	public AccountAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.accountVerificationAuthenticationTokenConverter = defaultConverter();
	}

	public AccountAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.accountVerificationAuthenticationTokenConverter = defaultConverter();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		AccountAuthenticationToken authRequest = accountVerificationAuthenticationTokenConverter.convert(
			request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, AccountAuthenticationToken> defaultConverter() {
		return request -> {
			String username = request.getParameter(this.usernameParameter);
			username = (username != null) ? username.trim() : "";

			String passord = request.getParameter(this.passwordParameter);
			passord = (passord != null) ? passord.trim() : "";

			String type = request.getParameter(this.typeParameter);
			type = (type != null) ? type.trim() : "";

			return new AccountAuthenticationToken(username, passord, type);
		};
	}


	protected void setDetails(HttpServletRequest request, AccountAuthenticationToken authRequest) {
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

	public void setConverter(Converter<HttpServletRequest, AccountAuthenticationToken> converter) {
		Assert.notNull(converter, "Converter must not be null");
		this.accountVerificationAuthenticationTokenConverter = converter;
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
