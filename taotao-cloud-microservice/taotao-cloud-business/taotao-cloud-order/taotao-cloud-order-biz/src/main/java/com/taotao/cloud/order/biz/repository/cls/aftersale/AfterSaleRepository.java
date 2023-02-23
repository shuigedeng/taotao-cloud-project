package com.taotao.cloud.order.biz.repository.cls.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

/**
 * 售后数据处理层
 *
 * @author shuigedeng
 */
public class AfterSaleRepository extends BaseClassSuperRepository<AfterSale, Long> {

	public AfterSaleRepository(EntityManager em) {
		super(AfterSale.class, em);
	}

}
