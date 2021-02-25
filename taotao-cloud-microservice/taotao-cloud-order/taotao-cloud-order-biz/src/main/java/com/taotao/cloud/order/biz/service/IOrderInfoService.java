package com.taotao.cloud.order.biz.service;


import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.biz.entity.Order;

/**
 * 订单管理service
 *
 * @author dengtao
 * @date 2020/4/30 11:03
 */
public interface IOrderInfoService {

    Order findOrderInfoByCode(String code);

    Order saveOrder(OrderDTO orderDTO);
}

