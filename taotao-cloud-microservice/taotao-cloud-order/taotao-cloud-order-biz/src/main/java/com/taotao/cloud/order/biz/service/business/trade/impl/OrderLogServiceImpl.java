package com.taotao.cloud.order.biz.service.business.trade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.api.model.query.order.OrderLogPageQuery;
import com.taotao.cloud.order.biz.mapper.trade.IOrderLogMapper;
import com.taotao.cloud.order.biz.model.entity.order.OrderLog;
import com.taotao.cloud.order.biz.service.business.trade.IOrderLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单日志业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:53
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<IOrderLogMapper, OrderLog> implements
	IOrderLogService {

	@Override
	public List<OrderLog> getOrderLog(String orderSn) {
		LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(OrderLog::getOrderSn, orderSn);
		return this.list(lambdaQueryWrapper);
	}

	@Override
	public IPage<OrderLog> pageQuery(OrderLogPageQuery orderLogPageQuery) {
		LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
		//todo 需要设置条件

		return this.page(orderLogPageQuery.buildMpPage(), lambdaQueryWrapper);
	}
}
