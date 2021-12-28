package com.taotao.cloud.stock.biz.domain.permission.model.enums;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang.StringUtils;

/**
 * 权限级别
 *
 * @author haoxin
 * @date 2021-02-15
 **/
public enum PermissionLevelEnum implements ValueObject<PermissionLevelEnum> {

    /**
     * 系统
     */
    SYSTEM("0", "系统"),

    /**
     * 租户
     */
    TENANT("1", "租户");


    private String value;

    private String label;

    PermissionLevelEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据匹配value的值获取Label
     *
     * @param value
     * @return
     */
    public static String getLabelByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        for (PermissionLevelEnum s : PermissionLevelEnum.values()) {
            if (value.equals(s.getValue())) {
                return s.getLabel();
            }
        }
        return "";
    }

    /**
     * 获取StatusEnum
     *
     * @param value
     * @return
     */
    public static PermissionLevelEnum getMenuLevelEnum(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (PermissionLevelEnum s : PermissionLevelEnum.values()) {
            if (value.equals(s.getValue())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean sameValueAs(final PermissionLevelEnum other) {
        return this.equals(other);
    }
}
