/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.stock.biz.domain.permission.model.enums;

import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 权限类型
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public enum PermissionTypeEnum implements ValueObject<PermissionTypeEnum> {

    /** 目录 */
    CATALOG("0", "目录"),

    /** 菜单 */
    MENU("1", "菜单"),

    /** 按钮 */
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
