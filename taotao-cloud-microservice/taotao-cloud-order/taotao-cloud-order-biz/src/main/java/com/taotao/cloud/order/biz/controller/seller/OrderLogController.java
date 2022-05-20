package com.taotao.cloud.order.biz.controller.seller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.biz.entity.order.OrderLog;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import com.taotao.cloud.order.biz.service.trade.IOrderLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	public Result<List<OrderLog>> get(@PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderLogService.getOrderLog(orderSn));
	}
}
