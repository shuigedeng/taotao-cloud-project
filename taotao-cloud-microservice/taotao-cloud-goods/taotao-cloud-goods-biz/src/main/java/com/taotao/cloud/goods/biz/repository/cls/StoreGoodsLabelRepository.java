package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.StoreGoodsLabel;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class StoreGoodsLabelRepository extends BaseCrSuperRepository<StoreGoodsLabel, Long> {

	public StoreGoodsLabelRepository(EntityManager em) {
		super(StoreGoodsLabel.class, em);
	}
}
