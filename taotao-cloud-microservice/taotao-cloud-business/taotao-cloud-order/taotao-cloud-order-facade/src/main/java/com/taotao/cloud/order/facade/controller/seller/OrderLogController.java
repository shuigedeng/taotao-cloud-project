/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.order.facade.controller.seller;

import com.taotao.boot.common.model.Result;
import com.taotao.cloud.order.application.service.order.IOrderService;
import com.taotao.cloud.order.application.service.trade.IOrderLogService;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderLogPO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,订单日志API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:40
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-订单日志API", description = "店铺端-订单日志API")
@RequestMapping("/order/seller/order/log")
public class OrderLogController {

	private final IOrderLogService orderLogService;

	private final IOrderService orderService;

	@Operation(summary = "通过订单编号获取订单日志", description = "通过订单编号获取订单日志")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{orderSn}")
	public Result<List<OrderLogPO>> get(@PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderLogService.getOrderLog(orderSn));
	}
}
