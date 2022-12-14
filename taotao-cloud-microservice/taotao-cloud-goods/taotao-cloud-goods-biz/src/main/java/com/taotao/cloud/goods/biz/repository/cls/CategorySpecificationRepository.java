package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class CategorySpecificationRepository extends
	BaseClassSuperRepository<CategorySpecification, Long> {

	public CategorySpecificationRepository(EntityManager em) {
		super(CategorySpecification.class, em);
	}
}
