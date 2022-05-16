package com.taotao.cloud.order.biz.service.order;


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
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:54:44
	 */
	Boolean updatePrice(String orderSn, BigDecimal orderPrice);

	/**
	 * 管理员订单付款
	 *
	 * @param orderSn 订单编号
	 * @return {@link Boolean }
	 * @since 2022-05-16 16:58:31
	 */
	Boolean adminPayOrder(String orderSn);
}
