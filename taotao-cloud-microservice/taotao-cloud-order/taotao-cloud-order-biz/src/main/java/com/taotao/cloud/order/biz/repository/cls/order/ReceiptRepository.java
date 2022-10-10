package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 发票数据处理层
 */
public class ReceiptRepository extends BaseCrSuperRepository<Receipt, Long> {

	public ReceiptRepository(EntityManager em) {
		super(Receipt.class, em);
	}


}
