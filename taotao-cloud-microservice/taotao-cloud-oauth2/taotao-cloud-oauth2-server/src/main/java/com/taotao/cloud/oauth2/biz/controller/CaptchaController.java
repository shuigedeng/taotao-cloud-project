package com.taotao.cloud.oauth2.biz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CaptchaController {

	@GetMapping("/captcha/login")
	public String login() {
		return "loginTmp";
	}


}
