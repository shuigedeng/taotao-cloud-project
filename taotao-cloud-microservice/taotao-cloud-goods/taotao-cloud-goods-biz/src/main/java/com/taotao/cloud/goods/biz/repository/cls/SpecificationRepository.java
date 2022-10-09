package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.web.base.repository.BaseCrSuperRepository;

import javax.persistence.EntityManager;

public class SpecificationRepository extends BaseCrSuperRepository<Specification, Long> {

	public SpecificationRepository(EntityManager em) {
		super(Specification.class, em);
	}
}
