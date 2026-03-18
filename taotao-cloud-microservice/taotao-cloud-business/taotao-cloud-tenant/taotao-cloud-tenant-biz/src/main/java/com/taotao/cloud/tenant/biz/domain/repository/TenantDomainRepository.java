package com.taotao.cloud.tenant.biz.domain.repository;

import com.taotao.boot.ddd.model.domain.repository.light.LightDomainRepository;
import com.taotao.cloud.tenant.biz.domain.aggregate.TenantAgg;

public interface TenantDomainRepository extends LightDomainRepository<TenantAgg> {

}
