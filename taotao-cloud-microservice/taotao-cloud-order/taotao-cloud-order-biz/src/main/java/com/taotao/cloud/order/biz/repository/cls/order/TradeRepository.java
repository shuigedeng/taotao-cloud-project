package com.taotao.cloud.order.biz.repository.cls.order;

import com.taotao.cloud.order.biz.model.entity.order.Trade;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

/**
 * 交易数据处理层
 */
public class TradeRepository extends BaseCrSuperRepository<Trade, Long> {

	public TradeRepository(EntityManager em) {
		super(Trade.class, em);
	}

}
