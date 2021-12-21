package com.taotao.cloud.oauth2.biz.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.oauth2.biz.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 短信API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 20:52:24
 */
@Controller
@RequestMapping("/oauth2")
public class SmsController {

	@Autowired
	private SmsService smsService;

	@GetMapping("/sms")
	public Result<Boolean> snedSms(String phoneNumber) {
		boolean result = smsService.sendSms(phoneNumber);
		return Result.success(result);
	}

}
