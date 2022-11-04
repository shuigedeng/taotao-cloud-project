package com.taotao.cloud.auth.biz.authentication.phone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.cloud.auth.biz.authentication.phone.PhoneAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "phone";

	public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/login/phone",
		"POST");

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

	private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;

	private Converter<HttpServletRequest, PhoneAuthenticationToken> captchaAuthenticationTokenConverter;

	private boolean postOnly = true;


	public PhoneAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.captchaAuthenticationTokenConverter = defaultConverter();
	}

	public PhoneAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		this.captchaAuthenticationTokenConverter = defaultConverter();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		PhoneAuthenticationToken authRequest = captchaAuthenticationTokenConverter.convert(request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, PhoneAuthenticationToken> defaultConverter() {
		return request -> {
			String username = request.getParameter(this.usernameParameter);
			username = (username != null) ? username.trim() : "";
			String captcha = request.getParameter(this.captchaParameter);
			captcha = (captcha != null) ? captcha.trim() : "";
			return new PhoneAuthenticationToken(username, captcha);
		};
	}


	protected void setDetails(HttpServletRequest request, PhoneAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	public void setCaptchaParameter(String captchaParameter) {
		Assert.hasText(captchaParameter, "Password parameter must not be empty or null");
		this.captchaParameter = captchaParameter;
	}

	public void setConverter(Converter<HttpServletRequest, PhoneAuthenticationToken> converter) {
		Assert.notNull(converter, "Converter must not be null");
		this.captchaAuthenticationTokenConverter = converter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getUsernameParameter() {
		return this.usernameParameter;
	}

	public final String getCaptchaParameter() {
		return this.captchaParameter;
	}
}
