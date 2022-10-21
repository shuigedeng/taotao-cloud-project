package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.StoreGoodsLabel;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

public class StoreGoodsLabelRepository extends BaseClassSuperRepository<StoreGoodsLabel, Long> {

	public StoreGoodsLabelRepository(EntityManager em) {
		super(StoreGoodsLabel.class, em);
	}
}
