package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.StudioCommodity;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class StudioCommodityRepository extends BaseCrSuperRepository<StudioCommodity, Long> {

	public StudioCommodityRepository(EntityManager em) {
		super(StudioCommodity.class, em);
	}
}
