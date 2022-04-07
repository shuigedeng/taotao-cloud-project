package com.taotao.cloud.order.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import com.taotao.cloud.order.biz.service.order.OrderService;
import com.taotao.cloud.order.biz.service.trade.OrderLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,订单日志接口
 **/

@Validated
@RestController
@Tag(name = "店铺端-订单日志API", description = "店铺端-订单日志API")
@RequestMapping("/order/seller/orderLog")
public class OrderLogController {

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderService orderService;
	
	@Operation(summary = "通过订单编号获取订单日志", description = "通过订单编号获取订单日志", method = CommonConstant.GET)
	@RequestLogger("通过订单编号获取订单日志")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{orderSn}")
	public Result<List<OrderLog>> get(@PathVariable String orderSn) {
		OperationalJudgment.judgment(orderService.getBySn(orderSn));
		return Result.success(orderLogService.getOrderLog(orderSn));
	}
}
