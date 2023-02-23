package com.taotao.cloud.order.biz.repository.cls.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

/**
 * 售后原因数据处理层
 */
public class AfterSaleReasonRepository extends BaseClassSuperRepository<AfterSaleReason, Long> {

	public AfterSaleReasonRepository(EntityManager em) {
		super(AfterSaleReason.class, em);
	}


}
