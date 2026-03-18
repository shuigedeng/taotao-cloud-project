package com.taotao.cloud.sys.biz.infrastructure.repository.domain;

import com.taotao.boot.ddd.model.domain.repository.light.BaseLightDomainRepository;
import com.taotao.cloud.sys.biz.domain.agg.UserAgg;
import com.taotao.cloud.sys.biz.domain.repository.UserDomainRepository;

public class UserRepositoryImpl extends BaseLightDomainRepository<UserAgg, Long, UserMapper> implements
	UserDomainRepository {

	@Override
	public Long save( UserAgg aggregateRoot ) {
		return super.save(aggregateRoot);
	}
}
