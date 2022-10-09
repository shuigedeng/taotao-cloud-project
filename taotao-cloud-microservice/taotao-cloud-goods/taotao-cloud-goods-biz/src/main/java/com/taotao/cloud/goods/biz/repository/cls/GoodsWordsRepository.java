package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.GoodsWords;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class GoodsWordsRepository extends BaseCrSuperRepository<GoodsWords, Long> {

	public GoodsWordsRepository(EntityManager em) {
		super(GoodsWords.class, em);
	}
}
