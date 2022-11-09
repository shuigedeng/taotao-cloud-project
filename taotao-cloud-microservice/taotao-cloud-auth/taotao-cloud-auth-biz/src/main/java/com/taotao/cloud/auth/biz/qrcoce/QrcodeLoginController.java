package com.taotao.cloud.auth.biz.qrcoce;

import cn.hutool.extra.qrcode.QrCodeUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Validated
@Tag(name = "二维码扫码登录API", description = "二维码扫码登录API")
@RestController
@RequestMapping("/login/qrcode")
public class QrcodeLoginController {

	@Autowired
	private QrCodeLoginService qrCodeLoginService;

	@RequestMapping(value = "/code", method = RequestMethod.GET)
	public void createCodeImg(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");

		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		try {
			//这里没啥操作 就是生成一个UUID插入 数据库的表里
			String uuid = qrCodeLoginService.generateUUID();
			response.setHeader("uuid", uuid);
			// 这里是开源工具类 hutool里的QrCodeUtil
			// 网址：http://hutool.mydoc.io/
			QrCodeUtil.generate(uuid, 300, 300, "jpg", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
