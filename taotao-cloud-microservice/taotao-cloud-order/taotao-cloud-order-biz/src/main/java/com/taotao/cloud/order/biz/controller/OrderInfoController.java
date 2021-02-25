package com.taotao.cloud.order.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.vo.OrderVO;
import com.taotao.cloud.order.biz.entity.Order;
import com.taotao.cloud.order.biz.mapper.OrderMapper;
import com.taotao.cloud.order.biz.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
 * @author dengtao
 * @date 2020/4/30 11:03
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/order")
@Api(value = "订单管理API", tags = {"订单管理API"})
public class OrderInfoController {

	private final IOrderInfoService orderInfoService;

	@ApiOperation("获取订单信息")
	@GetMapping("/info/{code}")
	@RequestOperateLog(description = "获取订单信息")
	@PreAuthorize("hasAuthority('order:info:code')")
	public Result<OrderVO> findOrderInfoByCode(@PathVariable("code") String code) {
		Order order = orderInfoService.findOrderInfoByCode(code);
		OrderVO vo = OrderMapper.INSTANCE.orderToOrderVO(order);
		return Result.succeed(vo);
	}

	@ApiOperation("添加订单信息")
	@PostMapping
	@RequestOperateLog(description = "添加订单信息")
	Result<OrderVO> saveOrder(@Validated @RequestBody OrderDTO orderDTO) {
		Order order = orderInfoService.saveOrder(orderDTO);
		OrderVO vo = OrderMapper.INSTANCE.orderToOrderVO(order);
		return Result.succeed(vo);
	}

}

