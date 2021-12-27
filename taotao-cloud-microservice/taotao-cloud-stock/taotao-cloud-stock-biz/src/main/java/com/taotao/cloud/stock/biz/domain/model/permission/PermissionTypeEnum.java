package com.taotao.cloud.stock.biz.domain.model.permission;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang.StringUtils;

/**
 * 权限类型
 *
 * @author haoxin
 * @date 2021-02-08
 **/
public enum PermissionTypeEnum implements ValueObject<PermissionTypeEnum> {

    /**
     * 目录
     */
    CATALOG("0", "目录"),

    /**
     * 菜单
     */
    MENU("1", "菜单"),

    /**
     * 按钮
     */
    BUTTON("2", "按钮");


    private String value;

    private String label;

    PermissionTypeEnum(String value, String label) {
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
        for (PermissionTypeEnum s : PermissionTypeEnum.values()) {
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
    public static PermissionTypeEnum getMenuTypeEnum(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (PermissionTypeEnum s : PermissionTypeEnum.values()) {
            if (value.equals(s.getValue())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean sameValueAs(final PermissionTypeEnum other) {
        return this.equals(other);
    }
}
