package com.taotao.cloud.order.biz.repository.cls.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 售后原因数据处理层
 */
public class AfterSaleReasonRepository extends BaseCrSuperRepository<AfterSaleReason, Long> {

	public AfterSaleReasonRepository(EntityManager em) {
		super(AfterSaleReason.class, em);
	}


}
