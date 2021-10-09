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

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.order.api.dto.OrderItemDTO;
import com.taotao.cloud.order.biz.entity.OrderItem;
import com.taotao.cloud.order.biz.service.IOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单项管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:24:26
 */
@Validated
@RestController
@RequestMapping("/order/item")
@Tag(name = "订单项管理API", description = "订单项管理API")
public class OrderItemController {

	private final IOrderItemService orderItemService;

	public OrderItemController(IOrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	/**
	 * 添加订单项信息
	 *
	 * @param orderItemDTO 订单项DTO
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 16:26:27
	 */
	@Operation(summary = "添加订单项信息", description = "添加订单项信息", method = CommonConstant.POST)
	@RequestOperateLog(description = "添加订单项信息")
	@PostMapping("/save")
	@SentinelResource(value = "saveOrderItem")
	Result<Boolean> saveOrderItem(@Validated @RequestBody OrderItemDTO orderItemDTO) {
		OrderItem orderItem = orderItemService.saveOrderItem(orderItemDTO);
		return Result.success(null != orderItem);
	}
}

