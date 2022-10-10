package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderComplaint;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 订单投诉数据处理层
 */
public class OrderComplaintRepository extends BaseCrSuperRepository<OrderComplaint, Long> {

	public OrderComplaintRepository(EntityManager em) {
		super(OrderComplaint.class, em);
	}

}
