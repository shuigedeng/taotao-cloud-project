package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.CustomWords;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

public class CustomWordsRepository extends BaseClassSuperRepository<CustomWords, Long> {

	public CustomWordsRepository(EntityManager em) {
		super(CustomWords.class, em);
	}
}
