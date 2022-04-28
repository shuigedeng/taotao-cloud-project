package com.taotao.cloud.order.biz.service.order;


import com.taotao.cloud.order.biz.entity.order.Order;

import java.math.BigDecimal;

/**
 * 订单价格
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:44
 */
public interface IOrderPriceService {

	/**
	 * 价格修改
	 * 日志功能内部实现
	 *
	 * @param orderSn    订单编号
	 * @param orderPrice 订单价格
	 * @return {@link Order }
	 * @since 2022-04-28 08:54:44
	 */
	Order updatePrice(String orderSn, BigDecimal orderPrice);

	/**
	 * 管理员订单付款
	 *
	 * @param orderSn 订单编号
	 * @since 2022-04-28 08:54:44
	 */
	void adminPayOrder(String orderSn);
}
