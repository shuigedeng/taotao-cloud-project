package com.taotao.cloud.order.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.service.IOrderInfoService;
import com.taotao.cloud.order.api.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @since 2020/4/30 11:03
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

	@Operation(summary = "获取订单信息", description = "获取订单信息", method = CommonConstant.GET)
	@GetMapping("/info/{code}")
	@RequestOperateLog(description = "获取订单信息")
	//@PreAuthorize("hasAuthority('order:info:code')")
	public Result<OrderVO> findOrderInfoByCode(@PathVariable("code") String code) {
		OrderVO vo = orderInfoService.findOrderInfoByCode(code);
		return Result.success(vo);
	}

	@Operation(summary = "添加订单信息", description = "添加订单信息", method = CommonConstant.POST)
	@PostMapping
	@RequestOperateLog(description = "添加订单信息")
	Result<OrderVO> saveOrder(@RequestBody OrderDTO orderDTO) {
		OrderVO vo = orderInfoService.saveOrder(orderDTO);
		return Result.success(vo);
	}

}

