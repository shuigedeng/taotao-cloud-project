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
package com.taotao.cloud.order.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.api.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:23:29
 */
@Validated
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理API", description = "订单管理API")
public class OrderInfoController {

	private final IOrderInfoService orderInfoService;

	public OrderInfoController(IOrderInfoService orderInfoService) {
		this.orderInfoService = orderInfoService;
	}

	/**
	 * 获取订单信息
	 *
	 * @param code 订单编码
	 * @return {@link Result&lt;com.taotao.cloud.order.api.vo.OrderVO&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 16:28:10
	 */
	@Operation(summary = "获取订单信息", description = "获取订单信息", method = CommonConstant.GET)
	@GetMapping("/info/{code}")
	@RequestOperateLog(description = "获取订单信息")
	//@PreAuthorize("hasAuthority('order:info:code')")
	public Result<OrderVO> findOrderInfoByCode(@PathVariable("code") String code) {
		OrderVO vo = orderInfoService.findOrderInfoByCode(code);
		return Result.success(vo);
	}

	/**
	 * 添加订单信息
	 *
	 * @param orderDTO 订单DTO
	 * @return {@link Result&lt;com.taotao.cloud.order.api.vo.OrderVO&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 16:27:45
	 */
	@Operation(summary = "添加订单信息", description = "添加订单信息", method = CommonConstant.POST)
	@PostMapping
	@RequestOperateLog(description = "添加订单信息")
	Result<OrderVO> saveOrder(@RequestBody OrderDTO orderDTO) {
		OrderVO vo = orderInfoService.saveOrder(orderDTO);
		return Result.success(vo);
	}

}

