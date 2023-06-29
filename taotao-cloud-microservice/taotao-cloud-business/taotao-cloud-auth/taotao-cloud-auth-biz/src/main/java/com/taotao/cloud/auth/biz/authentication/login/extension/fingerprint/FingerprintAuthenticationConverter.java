package com.taotao.cloud.auth.biz.authentication.login.extension.fingerprint;

import com.taotao.cloud.auth.biz.authentication.login.extension.account.AccountAuthenticationToken;
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
public class FingerprintAuthenticationConverter implements Converter<HttpServletRequest, FingerprintAuthenticationToken> {
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "name";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "fingerPrint";

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String fingerPrintParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	@Override
	public FingerprintAuthenticationToken convert(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);

		// username (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, usernameParameter);
		// password (REQUIRED)
		ExtensionLoginUtils.checkRequiredParameter(parameters, fingerPrintParameter);

		String username = request.getParameter(this.usernameParameter);
		String fingerPrint = request.getParameter(this.fingerPrintParameter);

		return FingerprintAuthenticationToken.unauthenticated(username, fingerPrint);
	}

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getFingerPrintParameter() {
		return fingerPrintParameter;
	}

	public void setFingerPrintParameter(String fingerPrintParameter) {
		this.fingerPrintParameter = fingerPrintParameter;
	}
}
