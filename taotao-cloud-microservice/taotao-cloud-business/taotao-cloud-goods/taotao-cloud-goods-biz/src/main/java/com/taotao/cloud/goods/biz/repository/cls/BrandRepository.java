package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Brand;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;

public class BrandRepository extends BaseClassSuperRepository<Brand, Long> {

	public BrandRepository(EntityManager em) {
		super(Brand.class, em);
	}
}
