package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Studio;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class StudioRepository extends BaseClassSuperRepository<Studio, Long> {

	public StudioRepository(EntityManager em) {
		super(Studio.class, em);
	}
}
