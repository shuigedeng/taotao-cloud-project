package com.taotao.cloud.auth.biz.authentication.accountVerification.service;

public interface AccountVerificationService {

	/**
	 * verify captcha
	 *
	 * @param verificationCode phone
	 * @return isVerified
	 */
	boolean verifyCaptcha(String verificationCode);
}
