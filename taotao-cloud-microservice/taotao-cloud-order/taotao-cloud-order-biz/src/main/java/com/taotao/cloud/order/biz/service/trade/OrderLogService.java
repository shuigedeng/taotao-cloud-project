package com.taotao.cloud.order.biz.service.trade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import java.util.List;

/**
 * 订单日志业务层
 */
public interface OrderLogService extends IService<OrderLog> {

	/**
	 * 根据订单编号获取订单日志列表
	 *
	 * @param orderSn 订单编号
	 * @return 订单日志列表
	 */
	List<OrderLog> getOrderLog(String orderSn);
}
