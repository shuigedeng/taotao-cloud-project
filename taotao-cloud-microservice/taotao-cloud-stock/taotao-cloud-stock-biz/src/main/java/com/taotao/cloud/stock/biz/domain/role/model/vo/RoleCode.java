package com.taotao.cloud.stock.biz.domain.role.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 角色编码
 *
 * @author shuigedeng
 * @date 2021-02-08
 */
public class RoleCode implements ValueObject<RoleCode> {

    /**
     * 租户管理员角色编码
     */
    public static final String TENANT_ADMIN = "tenantAdmin";

    private String code;

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    public RoleCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("角色编码不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(code).matches(),
                "编码格式不正确");
        this.code = code;
    }

    public boolean isTenantAdmin() {
        return TENANT_ADMIN.equals(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean sameValueAs(RoleCode other) {
        return other != null && this.code.equals(other.code);
    }

    @Override
    public String toString() {
        return code;
    }
}
