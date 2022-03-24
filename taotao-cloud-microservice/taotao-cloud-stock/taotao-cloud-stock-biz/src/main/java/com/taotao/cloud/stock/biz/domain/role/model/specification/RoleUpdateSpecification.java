package com.taotao.cloud.stock.biz.domain.role.model.specification;

import com.xtoon.cloud.common.core.domain.AbstractSpecification;
import com.xtoon.cloud.sys.domain.model.role.Role;

/**
 * 角色修改Specification
 *
 * @author shuigedeng
 * @date 2021-02-27
 **/
public class RoleUpdateSpecification extends AbstractSpecification<Role> {

    @Override
    public boolean isSatisfiedBy(Role role) {
        if (role.getRoleCode().isTenantAdmin()) {
            throw new RuntimeException("租户管理角色无法修改");
        }
        return true;
    }
}
