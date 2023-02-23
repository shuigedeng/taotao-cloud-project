package com.taotao.cloud.im.biz.platform.modules.common.controller;

import com.platform.common.constant.HeadConstant;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatVersionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用请求处理
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

	@Resource
	private ChatVersionService versionService;

	/**
	 * 校验版本号
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@GetMapping("/getVersion")
	public AjaxResult getVersion(HttpServletRequest request) {
		// 请求的版本
		String version = request.getHeader(HeadConstant.VERSION);
		// 请求的设备
		String device = request.getHeader(HeadConstant.DEVICE);
		return AjaxResult.success(versionService.getVersion(version, device));
	}

	/**
	 * 用户协议
	 */
	@ApiVersion(VersionEnum.V1_0_0)
	@GetMapping("/getAgreement")
	public AjaxResult getAgreement() {
		return AjaxResult.success(versionService.getAgreement());
	}

}
