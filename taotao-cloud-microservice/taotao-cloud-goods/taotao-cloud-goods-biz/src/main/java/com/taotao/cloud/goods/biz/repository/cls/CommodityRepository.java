package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Commodity;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class CommodityRepository extends BaseCrSuperRepository<Commodity, Long> {

	public CommodityRepository(EntityManager em) {
		super(Commodity.class, em);
	}
}
