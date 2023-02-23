package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Commodity;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class CommodityRepository extends BaseClassSuperRepository<Commodity, Long> {

	public CommodityRepository(EntityManager em) {
		super(Commodity.class, em);
	}
}
