package com.taotao.cloud.demo.seata.order.service;

import com.taotao.cloud.demo.seata.order.feign.AccountFeignClient;
import com.taotao.cloud.demo.seata.order.mapper.OrderMapper;
import com.taotao.cloud.demo.seata.order.model.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zlt
 * @since 2019/9/14
 */
@Service
public class OrderService {
	@Resource
	private AccountFeignClient accountFeignClient;

	@Resource
	private OrderMapper orderMapper;

	//@Transactional(rollbackFor = Exception.class)
	public void create(String userId, String commodityCode, Integer count) {
		//订单金额
		Integer orderMoney = count * 2;

		Order order = new Order()
			.setUserId(userId)
			.setCommodityCode(commodityCode)
			.setCount(count)
			.setMoney(orderMoney);
		orderMapper.insert(order);

		accountFeignClient.reduce(userId, orderMoney);
	}
}
