package com.taotao.cloud.order.biz.repository.cls.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuotedItem;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

/**
 * 采购单子内容数据处理层
 */
public class PurchaseQuotedItemRepository extends BaseClassSuperRepository<PurchaseQuotedItem, Long> {

	public PurchaseQuotedItemRepository(EntityManager em) {
		super(PurchaseQuotedItem.class, em);
	}


}
