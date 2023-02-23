package com.taotao.cloud.auth.biz.controller;

import com.taotao.cloud.auth.biz.service.CaptchaService;
import com.taotao.cloud.common.model.Result;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 20:41:56
 */
@Validated
@Tag(name = "验证码API", description = "验证码API")
@RestController
@RequestMapping("/auth/captcha")
public class CaptchaController {

	@Autowired
	private CaptchaService captchaService;

	@Operation(summary = "获取验证码", description = "获取验证码")
	// @RequestLogger
	@GetMapping("/code")
	public Result<String> getCaptcha(HttpServletRequest request) {
		ArithmeticCaptcha captcha = captchaService.getCaptcha(request);
		return Result.success(captcha.toBase64());
	}

}
