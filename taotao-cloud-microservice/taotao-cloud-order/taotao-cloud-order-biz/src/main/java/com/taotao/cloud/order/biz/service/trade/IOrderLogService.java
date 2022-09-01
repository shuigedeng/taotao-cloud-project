package com.taotao.cloud.order.biz.service.trade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.model.query.order.OrderLogPageQuery;
import com.taotao.cloud.order.biz.model.entity.order.OrderLog;

import java.util.List;

/**
 * 订单日志业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:50
 */
public interface IOrderLogService extends IService<OrderLog> {

	/**
	 * 根据订单编号获取订单日志列表
	 *
	 * @param orderSn 订单编号
	 * @return {@link List }<{@link OrderLog }>
	 * @since 2022-04-28 08:55:50
	 */
	List<OrderLog> getOrderLog(String orderSn);

    IPage<OrderLog> getByPage(OrderLogPageQuery orderLogPageQuery);

}
