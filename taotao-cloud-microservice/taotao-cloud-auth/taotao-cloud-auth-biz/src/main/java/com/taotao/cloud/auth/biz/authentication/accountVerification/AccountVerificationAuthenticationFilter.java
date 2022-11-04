package com.taotao.cloud.auth.biz.authentication.accountVerification;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountVerificationAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	public static final String SPRING_SECURITY_FORM_VERIFICATION_CODE_KEY = "verification_code";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/login/account/verification", "POST");

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	private String verificationCodeParameter = SPRING_SECURITY_FORM_VERIFICATION_CODE_KEY;

	private Converter<HttpServletRequest, AccountVerificationAuthenticationToken> accountVerificationAuthenticationTokenConverter;

	private boolean postOnly = true;

	public AccountVerificationAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.accountVerificationAuthenticationTokenConverter = defaultConverter();
	}

	public AccountVerificationAuthenticationFilter(AuthenticationManager authenticationManager) {
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

		AccountVerificationAuthenticationToken authRequest = accountVerificationAuthenticationTokenConverter.convert(request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, AccountVerificationAuthenticationToken> defaultConverter() {
		return request -> {
			String username = request.getParameter(this.usernameParameter);
			username = (username != null) ? username.trim() : "";

			String passord = request.getParameter(this.passwordParameter);
			passord = (passord != null) ? passord.trim() : "";

			String verificationCode = request.getParameter(this.verificationCodeParameter);
			verificationCode = (verificationCode != null) ? verificationCode.trim() : "";

			return new AccountVerificationAuthenticationToken(username, passord, verificationCode);
		};
	}


	protected void setDetails(HttpServletRequest request, AccountVerificationAuthenticationToken authRequest) {
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

	public void setVerificationCodeParameter(String verificationCodeParameter) {
		Assert.hasText(verificationCodeParameter, "verificationCode parameter must not be empty or null");
		this.verificationCodeParameter = verificationCodeParameter;
	}

	public void setConverter(Converter<HttpServletRequest, AccountVerificationAuthenticationToken> converter) {
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

	public String getVerificationCodeParameter() {
		return verificationCodeParameter;
	}
}
