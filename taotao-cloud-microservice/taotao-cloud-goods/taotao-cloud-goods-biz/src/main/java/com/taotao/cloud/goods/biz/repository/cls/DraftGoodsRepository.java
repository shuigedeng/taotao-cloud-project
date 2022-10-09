package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.DraftGoods;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class DraftGoodsRepository extends BaseCrSuperRepository<DraftGoods, Long> {

	public DraftGoodsRepository(EntityManager em) {
		super(DraftGoods.class, em);
	}
}
