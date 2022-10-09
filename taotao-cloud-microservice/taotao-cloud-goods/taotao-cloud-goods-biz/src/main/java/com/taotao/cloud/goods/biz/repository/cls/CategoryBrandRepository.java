package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.CategoryBrand;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class CategoryBrandRepository extends BaseCrSuperRepository<CategoryBrand, Long> {

	public CategoryBrandRepository(EntityManager em) {
		super(CategoryBrand.class, em);
	}
}
