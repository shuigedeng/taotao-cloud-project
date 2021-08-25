///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.captcha.controller;
//
//import com.taotao.cloud.captcha.model.CaptchaVO;
//import com.taotao.cloud.captcha.model.ResponseModel;
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
//	@PostMapping("/verify")
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
