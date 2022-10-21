package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.GoodsUnit;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

public class GoodsUnitRepository extends BaseClassSuperRepository<GoodsUnit, Long> {

	public GoodsUnitRepository(EntityManager em) {
		super(GoodsUnit.class, em);
	}
}
