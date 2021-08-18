///*
// *Copyright © 2018 anji-plus
// *安吉加加信息技术有限公司
// *http://www.anji-plus.com
// *All rights reserved.
// */
//package com.taotao.cloud.captcha.controller;
//
//import com.taotao.cloud.captcha.model.common.ResponseModel;
//import com.taotao.cloud.captcha.model.vo.CaptchaVO;
//import com.taotao.cloud.captcha.service.CaptchaService;
//import com.taotao.cloud.captcha.util.StringUtils;
//import javax.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/captcha")
//public class CaptchaController {
//
//	@Autowired
//	private CaptchaService captchaService;
//
//	@PostMapping("/get")
//	public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
//		assert request.getRemoteHost() != null;
//		data.setBrowserInfo(getRemoteId(request));
//		return captchaService.get(data);
//	}
//
//	@PostMapping("/check")
//	public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
//		data.setBrowserInfo(getRemoteId(request));
//		return captchaService.check(data);
//	}
//
//	//@PostMapping("/verify")
//	public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
//		return captchaService.verification(data);
//	}
//
//	public static final String getRemoteId(HttpServletRequest request) {
//		String xfwd = request.getHeader("X-Forwarded-For");
//		String ip = getRemoteIpFromXfwd(xfwd);
//		String ua = request.getHeader("user-agent");
//		if (StringUtils.isNotBlank(ip)) {
//			return ip + ua;
//		}
//		return request.getRemoteAddr() + ua;
//	}
//
//	private static String getRemoteIpFromXfwd(String xfwd) {
//		if (StringUtils.isNotBlank(xfwd)) {
//			String[] ipList = xfwd.split(",");
//			return StringUtils.trim(ipList[0]);
//		}
//		return null;
//	}
//
//}
