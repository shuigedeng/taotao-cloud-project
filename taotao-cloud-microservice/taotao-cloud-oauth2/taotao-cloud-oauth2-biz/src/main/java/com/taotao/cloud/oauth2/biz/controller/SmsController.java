package com.taotao.cloud.oauth2.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.oauth2.biz.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 20:52:24
 */
@Validated
@Tag(name = "短信API", description = "短信API")
@RestController
@RequestMapping("/oauth2")
public class SmsController {

	@Autowired
	private SmsService smsService;

	@Operation(summary = "发送短信", description = "发送短信", method = CommonConstant.POST)
	@RequestLogger(description = "发送短信")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@PostMapping("/sms")
	public Result<Boolean> sendSms(String phoneNumber) {
		boolean result = smsService.sendSms(phoneNumber);
		return Result.success(result);
	}

}
