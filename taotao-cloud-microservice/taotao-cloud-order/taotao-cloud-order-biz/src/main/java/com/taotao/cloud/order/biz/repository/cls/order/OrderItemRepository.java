package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderItem;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 子订单数据处理层
 */
public class OrderItemRepository extends BaseCrSuperRepository<OrderItem, Long> {

	public OrderItemRepository(EntityManager em) {
		super(OrderItem.class, em);
	}


}
