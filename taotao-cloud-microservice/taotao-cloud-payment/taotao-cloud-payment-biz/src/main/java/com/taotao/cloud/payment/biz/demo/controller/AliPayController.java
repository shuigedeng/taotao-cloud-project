package com.taotao.cloud.payment.biz.demo.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yungouos.springboot.demo.common.ApiResponse;
import com.yungouos.springboot.demo.service.alipay.AliPayService;

import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/api/alipay")
public class AliPayController {

	@Resource
	private AliPayService aliPayService;

	@ResponseBody
	@RequestMapping("/nativePay")
	public JSONObject nativePay(@RequestParam Map<String, String> data) {
		JSONObject response = ApiResponse.init();
		try {
			String body = data.get("body");
			String money = data.get("money");
			if (StrUtil.isBlank(body)) {
				response = ApiResponse.fail("body is not null");
				return response;
			}
			if (StrUtil.isBlank(money)) {
				response = ApiResponse.fail("money is not null");
				return response;
			}
			Map<String, Object> map = aliPayService.nativePay(body, money);
			response = ApiResponse.success("支付宝下单成功", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@ResponseBody
	@RequestMapping("/mobilePay")
	public JSONObject mobilePay(@RequestParam Map<String, String> data) {
		JSONObject response = ApiResponse.init();
		try {
			String body = data.get("body");
			String money = data.get("money");
			if (StrUtil.isBlank(body)) {
				response = ApiResponse.fail("body is not null");
				return response;
			}
			if (StrUtil.isBlank(money)) {
				response = ApiResponse.fail("money is not null");
				return response;
			}
			Map<String, Object> map = aliPayService.mobilePay(body, money);
			response = ApiResponse.success("支付宝下单成功", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
