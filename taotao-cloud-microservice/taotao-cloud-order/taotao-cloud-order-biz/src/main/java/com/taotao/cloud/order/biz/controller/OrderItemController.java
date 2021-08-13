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
 * 订单管理API
 *
 * @author shuigedeng
 * @since 2020/4/30 11:03
 */
@Validated
@RestController
@RequestMapping("/orderItem")
@Tag(name = "订单项管理API", description = "订单项管理API")
public class OrderItemController {

	private final IOrderItemService orderItemService;

	public OrderItemController(IOrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	@Operation(summary = "添加订单项信息", description = "添加订单项信息", method = CommonConstant.POST)
	@RequestOperateLog(description = "添加订单项信息")
	@PostMapping("/save")
	@SentinelResource(value = "saveOrderItem")
	Result<Boolean> saveOrderItem(@Validated @RequestBody OrderItemDTO orderItemDTO) {
		OrderItem orderItem = orderItemService.saveOrderItem(orderItemDTO);
		return Result.success(null != orderItem);
	}
}

