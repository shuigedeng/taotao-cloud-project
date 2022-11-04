package com.taotao.cloud.auth.biz.controller;

import com.taotao.cloud.auth.biz.service.QrCodeService;
import com.taotao.cloud.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 二维码API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 21:07:16
 */
@Validated
@Tag(name = "二维码API", description = "二维码API")
@RestController
@RequestMapping("/auth/qrcode")
public class QrCodeController {

	@Autowired
	private QrCodeService qrCodeService;

	@Operation(summary = "获取二维码", description = "获取二维码")
	// @RequestLogger
	@GetMapping("/code")
	public Result<String> qrcode() {
		return Result.success(qrCodeService.qrcode());
	}


}
