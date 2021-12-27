package com.taotao.cloud.stock.biz.domain.model.role;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 角色名称
 *
 * @author haoxin
 * @date 2021-02-08
 **/
public class RoleName implements ValueObject<RoleName> {

    /**
     * 租户管理员角色名称
     */
    public static final String TENANT_ADMIN = "租户管理";

    private String name;

    public RoleName(final String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sameValueAs(RoleName other) {
        return other != null && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
