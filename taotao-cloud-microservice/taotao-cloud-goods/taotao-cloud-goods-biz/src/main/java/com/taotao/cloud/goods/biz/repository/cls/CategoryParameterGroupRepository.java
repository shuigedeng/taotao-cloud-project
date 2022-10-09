package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.CategoryParameterGroup;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class CategoryParameterGroupRepository extends BaseCrSuperRepository<CategoryParameterGroup, Long> {

	public CategoryParameterGroupRepository(EntityManager em) {
		super(CategoryParameterGroup.class, em);
	}
}
