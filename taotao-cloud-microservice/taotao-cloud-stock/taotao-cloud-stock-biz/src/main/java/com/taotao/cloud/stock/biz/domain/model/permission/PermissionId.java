package com.taotao.cloud.stock.biz.domain.model.permission;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 权限ID
 *
 * @author haoxin
 * @date 2021-02-08
 **/
public class PermissionId implements ValueObject<PermissionId> {

    private String id;

    public PermissionId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("权限id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean sameValueAs(PermissionId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
