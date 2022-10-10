package com.taotao.cloud.order.biz.repository.cls.aftersale;

import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 售后日志数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:21
 */
public class AfterSaleLogRepository extends BaseCrSuperRepository<AfterSaleLog, Long> {

	public AfterSaleLogRepository(EntityManager em) {
		super(AfterSaleLog.class, em);
	}


}
