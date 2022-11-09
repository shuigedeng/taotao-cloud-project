package com.taotao.cloud.auth.biz.authentication.phone.service;

public class DefaultPhoneService implements PhoneService {
	@Override
	public boolean verifyCaptcha(String phone, String rawCode) {
		return false;
	}
}
