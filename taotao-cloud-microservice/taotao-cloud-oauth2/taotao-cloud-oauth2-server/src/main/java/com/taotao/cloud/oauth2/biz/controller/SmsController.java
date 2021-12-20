package com.taotao.cloud.oauth2.biz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SmsController {

	@GetMapping("/sms/send")
	public String login() {
		return "loginTmp";
	}


}
