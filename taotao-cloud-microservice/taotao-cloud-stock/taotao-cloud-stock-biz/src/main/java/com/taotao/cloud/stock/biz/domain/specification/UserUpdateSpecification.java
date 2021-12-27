package com.taotao.cloud.stock.biz.domain.specification;

import com.xtoon.cloud.common.core.domain.AbstractSpecification;
import com.xtoon.cloud.sys.domain.model.tenant.Tenant;
import com.xtoon.cloud.sys.domain.model.tenant.TenantRepository;
import com.xtoon.cloud.sys.domain.model.user.User;

/**
 * 用户修改Specification
 *
 * @author haoxin
 * @date 2021-02-27
 **/
public class UserUpdateSpecification extends AbstractSpecification<User> {

    private TenantRepository tenantRepository;

    public UserUpdateSpecification(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean isSatisfiedBy(User user) {
        Tenant tenant = tenantRepository.find(user.getTenantId());
        if (tenant.getCreatorId().sameValueAs(user.getUserId())) {
            throw new RuntimeException("租户创建者无法修改");
        }
        return false;
    }
}
