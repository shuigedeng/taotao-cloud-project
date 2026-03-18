package com.taotao.cloud.tenant.biz.domain.service.impl;

import com.taotao.cloud.tenant.biz.domain.repository.TenantDomainRepository;
import com.taotao.cloud.tenant.biz.domain.service.TenantDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantDomainServiceImpl implements TenantDomainService {
private final TenantDomainRepository tenantDomainRepository;
}
