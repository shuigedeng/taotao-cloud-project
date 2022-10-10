package com.taotao.cloud.order.biz.repository.cls.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseOrder;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 采购单数据处理层
 */
public class PurchaseOrderRepository extends BaseCrSuperRepository<PurchaseOrder, Long> {

	public PurchaseOrderRepository(EntityManager em) {
		super(PurchaseOrder.class, em);
	}


}
