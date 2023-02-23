package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.DraftGoods;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class DraftGoodsRepository extends BaseClassSuperRepository<DraftGoods, Long> {

	public DraftGoodsRepository(EntityManager em) {
		super(DraftGoods.class, em);
	}
}
