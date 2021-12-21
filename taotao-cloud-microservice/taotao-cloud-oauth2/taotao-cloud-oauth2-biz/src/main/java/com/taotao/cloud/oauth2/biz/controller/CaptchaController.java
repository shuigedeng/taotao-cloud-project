package com.taotao.cloud.oauth2.biz.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.oauth2.biz.service.CaptchaService;
import com.wf.captcha.ArithmeticCaptcha;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 验证码API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 20:41:56
 */
@Controller
@RequestMapping("/oauth2")
public class CaptchaController {

	@Autowired
	private CaptchaService captchaService;

	@GetMapping("/captcha")
	public Result<String> getCaptcha(HttpServletRequest request) {
		ArithmeticCaptcha captcha = captchaService.getCaptcha(request);
		return Result.success(captcha.toBase64());
	}

}
