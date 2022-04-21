package com.taotao.cloud.stock.biz.domain.tenant.model.specification;

import com.xtoon.cloud.common.core.domain.AbstractSpecification;
import com.xtoon.cloud.sys.domain.model.tenant.Tenant;
import com.xtoon.cloud.sys.domain.model.tenant.TenantRepository;

/**
 * 租户创建Specification
 *
 * @author shuigedeng
 * @date 2021-03-29
 */
public class TenantCreateSpecification extends AbstractSpecification<Tenant> {

    private TenantRepository tenantRepository;

    public TenantCreateSpecification(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean isSatisfiedBy(Tenant tenant) {
        if (tenantRepository.find(tenant.getTenantName()) != null) {
            throw new IllegalArgumentException("租户名称已存在");
        }
        if (tenantRepository.find(tenant.getTenantCode()) != null) {
            throw new IllegalArgumentException("租户编码已存在");
        }
        return true;
    }
}
