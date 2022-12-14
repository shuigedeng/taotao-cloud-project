package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderComplaint;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

/**
 * 订单投诉数据处理层
 */
public class OrderComplaintRepository extends BaseClassSuperRepository<OrderComplaint, Long> {

	public OrderComplaintRepository(EntityManager em) {
		super(OrderComplaint.class, em);
	}

}
