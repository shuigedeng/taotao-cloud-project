package com.taotao.cloud.order.api.service;


import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.vo.OrderVO;

/**
 * 订单管理service
 *
 * @author shuigedeng
 * @since 2020/4/30 11:03
 */
public interface IOrderInfoService {

	OrderVO findOrderInfoByCode(String code);

	OrderVO saveOrder(OrderDTO orderDTO);
}

