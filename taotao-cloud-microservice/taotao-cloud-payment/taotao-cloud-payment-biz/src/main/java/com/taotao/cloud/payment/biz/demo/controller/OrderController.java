package com.taotao.cloud.payment.biz.demo.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yungouos.springboot.demo.common.ApiResponse;
import com.yungouos.springboot.demo.entity.Order;
import com.yungouos.springboot.demo.service.order.OrderService;

import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Resource
	private OrderService orderService;

	
	@RequestMapping("/checkOrderStatus")
	@ResponseBody
	public JSONObject checkOrderStatus(@RequestParam Map<String, String> data) {
		JSONObject response = ApiResponse.init();
		try {
			String orderNo = data.get("orderNo");
			if (StrUtil.isBlank(orderNo)) {
				response = ApiResponse.fail("订单号不能为空");
				return response;
			}
			Order order = orderService.getOrderInfo(orderNo);

			if (order.getStatus().intValue() == 0) {
				response = ApiResponse.success("查询成功", false);
				return response;
			}
			response = ApiResponse.success("查询成功", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
