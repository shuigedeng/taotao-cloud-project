package com.taotao.cloud.stock.biz.domain.permission.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限名称
 *
 * @author haoxin
 * @date 2021-02-08
 **/
public class PermissionName implements ValueObject<PermissionName> {

    private String name;

    public PermissionName(final String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("权限名称不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sameValueAs(PermissionName other) {
        return other != null && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
