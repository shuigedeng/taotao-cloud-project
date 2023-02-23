package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.CategoryParameterGroup;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class CategoryParameterGroupRepository extends
		BaseClassSuperRepository<CategoryParameterGroup, Long> {

	public CategoryParameterGroupRepository(EntityManager em) {
		super(CategoryParameterGroup.class, em);
	}
}
