package com.taotao.cloud.payment.biz.demo.service.order;

import com.yungouos.springboot.demo.entity.Order;

public interface OrderService {

	// 保存订单
	public Order add(String body, String money);

	// 根据订单号查询订单详情
	public Order getOrderInfo(String orderNo);

	// 订单支付成功
	public boolean paySuccess(String orderNo, String payNo, String payTime);
}
