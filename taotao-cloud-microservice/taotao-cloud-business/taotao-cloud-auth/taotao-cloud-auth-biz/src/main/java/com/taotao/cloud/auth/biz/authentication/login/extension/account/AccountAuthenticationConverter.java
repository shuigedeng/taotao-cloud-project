package com.taotao.cloud.auth.biz.authentication.login.extension.account;

import com.taotao.cloud.auth.biz.authentication.utils.ExtensionLoginUtils;
import com.taotao.cloud.common.enums.LoginTypeEnum;
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
public class AccountAuthenticationConverter implements Converter<HttpServletRequest, AccountAuthenticationToken> {
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
	public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	/**
	 * @see LoginTypeEnum B_PC_ACCOUNT / C_PC_ACCOUNT
	 */
	private String typeParameter = SPRING_SECURITY_FORM_TYPE_KEY;

	@Override
	public AccountAuthenticationToken convert(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);

		// username (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, usernameParameter);
		// password (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, passwordParameter);
		// type (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, typeParameter);

		String username = request.getParameter(this.usernameParameter);
		String password = request.getParameter(this.passwordParameter);
		String type = request.getParameter(this.typeParameter);

		return AccountAuthenticationToken.unauthenticated(username, password, type);
	}

	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public final String getUsernameParameter() {
		return this.usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}
}
