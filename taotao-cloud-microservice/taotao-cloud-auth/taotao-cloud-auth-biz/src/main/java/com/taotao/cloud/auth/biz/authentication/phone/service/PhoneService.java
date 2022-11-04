package com.taotao.cloud.auth.biz.authentication.phone.service;

public interface PhoneService {

	/**
	 * verify captcha
	 *
	 * @param phone   phone
	 * @param rawCode rawCode
	 * @return isVerified
	 */
	boolean verifyCaptcha(String phone, String rawCode);
}
