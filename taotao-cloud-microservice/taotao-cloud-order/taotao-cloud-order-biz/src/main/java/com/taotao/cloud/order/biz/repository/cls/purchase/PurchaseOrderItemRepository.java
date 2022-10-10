package com.taotao.cloud.order.biz.repository.cls.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseOrderItem;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 采购单子内容数据处理层
 */
public class PurchaseOrderItemRepository extends BaseCrSuperRepository<PurchaseOrderItem, Long> {

	public PurchaseOrderItemRepository(EntityManager em) {
		super(PurchaseOrderItem.class, em);
	}


}
