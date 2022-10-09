package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class CategorytRepository extends BaseCrSuperRepository<Category, Long> {

	public CategorytRepository(EntityManager em) {
		super(Category.class, em);
	}
}
