package com.taotao.cloud.auth.biz.authentication.phone.service;

import com.taotao.cloud.message.api.feign.IFeignNoticeMessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPhoneService implements PhoneService {
	@Autowired
	private IFeignNoticeMessageApi feignNoticeMessageApi;

	@Override
	public boolean verifyCaptcha(String phone, String rawCode) {
		//校验短信验证码
		return false;
	}
}
