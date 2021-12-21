package com.taotao.cloud.oauth2.biz.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.oauth2.biz.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 二维码API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-21 21:07:16
 */
@Controller
@RequestMapping("/oauth2")
public class QrCodeController {

	@Autowired
	private QrCodeService qrCodeService;

	@GetMapping("/qrcode")
	public Result<String> qrcode() {
		return Result.success(qrCodeService.qrcode());
	}


}
