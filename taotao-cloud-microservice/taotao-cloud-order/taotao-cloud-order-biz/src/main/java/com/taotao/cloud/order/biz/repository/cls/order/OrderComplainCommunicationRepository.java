package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

/**
 * 交易投诉通信数据处理层
 */
public class OrderComplainCommunicationRepository extends
		BaseClassSuperRepository<OrderComplaintCommunication, Long> {

	public OrderComplainCommunicationRepository(EntityManager em) {
		super(OrderComplaintCommunication.class, em);
	}


}
