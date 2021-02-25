package com.taotao.cloud.demo.seata.business.controller;


import com.taotao.cloud.demo.seata.business.service.BusinessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zlt
 * @since 2019/9/14
 */
@RestController
public class BusinessController {
	@Resource
	private BusinessService businessService;

	/**
	 * 下单场景测试-正常
	 */
	@RequestMapping(path = "/placeOrder")
	public Boolean placeOrder() {
		businessService.placeOrder("U001");
		return true;
	}

	/**
	 * 下单场景测试-回滚
	 */
	@RequestMapping(path = "/placeOrderFallBack")
	public Boolean placeOrderFallBack() {
		businessService.placeOrder("U002");
		return true;
	}
}
