package com.taotao.cloud.sys.biz.domain.repository;

import com.taotao.boot.ddd.model.domain.repository.light.LightDomainRepository;
import com.taotao.cloud.sys.biz.domain.agg.UserAgg;

public interface UserDomainRepository extends LightDomainRepository<UserAgg,Long> {

}
