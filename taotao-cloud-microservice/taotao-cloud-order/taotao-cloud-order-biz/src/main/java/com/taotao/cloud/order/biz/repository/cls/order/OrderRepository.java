package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

/**
 * 订单数据处理层
 */
public class OrderRepository extends BaseClassSuperRepository<Order, Long> {

	public OrderRepository(EntityManager em) {
		super(Order.class, em);
	}


}
