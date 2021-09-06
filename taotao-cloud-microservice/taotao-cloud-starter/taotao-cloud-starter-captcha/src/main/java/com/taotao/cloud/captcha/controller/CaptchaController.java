/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.captcha.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.captcha.dto.CheckCptchaDTO;
import com.taotao.cloud.captcha.dto.CheckCptchaVO;
import com.taotao.cloud.captcha.dto.GetCptchaDTO;
import com.taotao.cloud.captcha.dto.GetCptchaVO;
import com.taotao.cloud.captcha.model.Captcha;
import com.taotao.cloud.captcha.service.CaptchaService;
import com.taotao.cloud.common.model.Result;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CaptchaController 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:35:39
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	private CaptchaService captchaService;

	public CaptchaController(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	@PostMapping("/get")
	public Result<GetCptchaVO> get(@RequestBody GetCptchaDTO dto, HttpServletRequest request) {
		Captcha ca = new Captcha();
		ca.setBrowserInfo(getRemoteId(request));
		ca.setClientUid(dto.getClientUid());
		ca.setCaptchaType(dto.getCaptchaType());
		
		Captcha captcha = captchaService.get(ca);

		GetCptchaVO vo = new GetCptchaVO();
		BeanUtil.copyProperties(captcha, vo, true);
		return Result.success(vo);
	}

	@PostMapping("/check")
	public Result<CheckCptchaVO> check(@RequestBody CheckCptchaDTO dto,
		HttpServletRequest request) {
		Captcha ca = new Captcha();
		ca.setBrowserInfo(getRemoteId(request));
		ca.setCaptchaType(dto.getCaptchaType());
		ca.setPointJson(dto.getPointJson());
		ca.setToken(dto.getToken());

		Captcha captcha = captchaService.check(ca);
		
		CheckCptchaVO vo = new CheckCptchaVO();
		BeanUtil.copyProperties(captcha, vo, true);
		return Result.success(vo);
	}

	@PostMapping("/verify")
	public Result<Captcha> verify(@RequestBody Captcha data, HttpServletRequest request) {
		Captcha captcha = captchaService.verification(data);
		return Result.success(captcha);
	}

	public static String getRemoteId(HttpServletRequest request) {
		String xfwd = request.getHeader("X-Forwarded-For");
		String ip = getRemoteIpFromXfwd(xfwd);
		String ua = request.getHeader("user-agent");
		if (StrUtil.isNotBlank(ip)) {
			return ip + ua;
		}
		return request.getRemoteAddr() + ua;
	}

	private static String getRemoteIpFromXfwd(String xfwd) {
		if (StrUtil.isNotBlank(xfwd)) {
			String[] ipList = xfwd.split(",");
			return StrUtil.trim(ipList[0]);
		}
		return null;
	}

}
