package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.StoreFlow;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 商家订单流水数据处理层
 */
public class StoreFlowRepository extends BaseCrSuperRepository<StoreFlow, Long> {

	public StoreFlowRepository(EntityManager em) {
		super(StoreFlow.class, em);
	}
}
