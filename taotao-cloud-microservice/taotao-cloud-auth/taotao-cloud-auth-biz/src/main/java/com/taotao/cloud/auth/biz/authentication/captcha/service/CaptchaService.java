package com.taotao.cloud.auth.biz.authentication.captcha.service;

/**
 * @author n1
 */
public interface CaptchaService {

	/**
	 * verify captcha
	 *
	 * @param phone   phone
	 * @param rawCode rawCode
	 * @return isVerified
	 */
	boolean verifyCaptcha(String phone, String rawCode);
}
