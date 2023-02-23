package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class GoodsSkuRepository extends BaseClassSuperRepository<GoodsSku, Long> {

	public GoodsSkuRepository(EntityManager em) {
		super(GoodsSku.class, em);
	}
}
