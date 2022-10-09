package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class GoodsSkuRepository extends BaseCrSuperRepository<GoodsSku, Long> {

	public GoodsSkuRepository(EntityManager em) {
		super(GoodsSku.class, em);
	}
}
