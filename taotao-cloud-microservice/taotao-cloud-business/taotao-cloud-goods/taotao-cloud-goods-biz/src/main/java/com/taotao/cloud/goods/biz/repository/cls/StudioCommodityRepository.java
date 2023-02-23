package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.StudioCommodity;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class StudioCommodityRepository extends BaseClassSuperRepository<StudioCommodity, Long> {

	public StudioCommodityRepository(EntityManager em) {
		super(StudioCommodity.class, em);
	}
}
