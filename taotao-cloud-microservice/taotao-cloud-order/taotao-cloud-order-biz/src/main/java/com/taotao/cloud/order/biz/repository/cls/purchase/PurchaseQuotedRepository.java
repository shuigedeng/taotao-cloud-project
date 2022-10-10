package com.taotao.cloud.order.biz.repository.cls.purchase;


import com.taotao.cloud.order.biz.model.entity.purchase.PurchaseQuoted;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 采购报价数据处理层
 */
public class PurchaseQuotedRepository extends BaseCrSuperRepository<PurchaseQuoted, Long> {

	public PurchaseQuotedRepository(EntityManager em) {
		super(PurchaseQuoted.class, em);
	}


}
