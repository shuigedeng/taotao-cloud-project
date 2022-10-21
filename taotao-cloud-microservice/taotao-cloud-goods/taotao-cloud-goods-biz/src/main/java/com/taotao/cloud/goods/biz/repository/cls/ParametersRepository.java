package com.taotao.cloud.goods.biz.repository.cls;

import com.taotao.cloud.goods.biz.model.entity.Parameters;
import com.taotao.cloud.web.base.repository.BaseClassSuperRepository;

import javax.persistence.EntityManager;

public class ParametersRepository extends BaseClassSuperRepository<Parameters, Long> {

	public ParametersRepository(EntityManager em) {
		super(Parameters.class, em);
	}
}
