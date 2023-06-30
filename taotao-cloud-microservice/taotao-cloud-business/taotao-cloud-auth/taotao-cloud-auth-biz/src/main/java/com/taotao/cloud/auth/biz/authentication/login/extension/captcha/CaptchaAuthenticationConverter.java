package com.taotao.cloud.auth.biz.authentication.login.extension.captcha;

import com.taotao.cloud.auth.biz.authentication.utils.ExtensionLoginUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/**
 * 帐户验证转换器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 13:07:23
 */
public class CaptchaAuthenticationConverter implements Converter<HttpServletRequest, CaptchaAuthenticationToken> {
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	public static final String SPRING_SECURITY_FORM_VERIFICATION_CODE_KEY = "verification_code";
	public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	private String verificationCodeParameter = SPRING_SECURITY_FORM_VERIFICATION_CODE_KEY;
	private String typeParameter = SPRING_SECURITY_FORM_TYPE_KEY;

	@Override
	public CaptchaAuthenticationToken convert(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);

		// username (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, usernameParameter);
		// password (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, passwordParameter);
		// type (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, verificationCodeParameter);
		// type (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, typeParameter);

		String username = request.getParameter(this.usernameParameter);
		String password = request.getParameter(this.passwordParameter);
		String verificationCode = request.getParameter(this.verificationCodeParameter);
		String type = request.getParameter(this.typeParameter);

		return CaptchaAuthenticationToken.unauthenticated(username, password, verificationCode, type);
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

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public String getVerificationCodeParameter() {
		return verificationCodeParameter;
	}

	public String getTypeParameter() {
		return typeParameter;
	}

	public void setTypeParameter(String typeParameter) {
		this.typeParameter = typeParameter;
	}
}
