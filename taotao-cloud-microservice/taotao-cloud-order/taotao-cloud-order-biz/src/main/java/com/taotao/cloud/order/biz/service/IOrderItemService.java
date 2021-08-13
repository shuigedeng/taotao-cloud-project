package com.taotao.cloud.order.biz.service;


import com.taotao.cloud.order.api.dto.OrderItemDTO;
import com.taotao.cloud.order.biz.entity.OrderItem;

/**
 * 订单管理service
 *
 * @author shuigedeng
 * @since 2020/4/30 11:03
 */
public interface IOrderItemService {

	OrderItem findOrderItemByCode(String code);

	OrderItem saveOrderItem(OrderItemDTO orderItemDTO);
}

