package com.taotao.cloud.auth.biz.authentication.face;

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

public class FaceAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_IMAGE_BASE_64_KEY = "imgBase64";

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/login/face", "POST");

	private final String imgBase64Parameter = SPRING_SECURITY_FORM_IMAGE_BASE_64_KEY;

	private Converter<HttpServletRequest, FaceAuthenticationToken> accountVerificationAuthenticationTokenConverter;

	private boolean postOnly = true;

	public FaceAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
		this.accountVerificationAuthenticationTokenConverter = defaultConverter();
	}

	public FaceAuthenticationFilter(AuthenticationManager authenticationManager) {
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

		FaceAuthenticationToken authRequest = accountVerificationAuthenticationTokenConverter.convert(
			request);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}


	private Converter<HttpServletRequest, FaceAuthenticationToken> defaultConverter() {
		return request -> {
			String imgBase64 = request.getParameter(this.imgBase64Parameter);
			imgBase64 = (imgBase64 != null) ? imgBase64.trim() : "";


			return new FaceAuthenticationToken(imgBase64);
		};
	}


	protected void setDetails(HttpServletRequest request, FaceAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	public void setConverter(Converter<HttpServletRequest, FaceAuthenticationToken> converter) {
		Assert.notNull(converter, "Converter must not be null");
		this.accountVerificationAuthenticationTokenConverter = converter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}


}
