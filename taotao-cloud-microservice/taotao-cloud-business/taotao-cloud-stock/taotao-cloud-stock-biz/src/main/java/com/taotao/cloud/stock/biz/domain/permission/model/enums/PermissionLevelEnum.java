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

import org.apache.commons.lang3.StringUtils;

/**
 * 权限级别
 *
 * @author shuigedeng
 * @since 2021-02-15
 */
public enum PermissionLevelEnum implements ValueObject<PermissionLevelEnum> {

    /** 系统 */
    SYSTEM("0", "系统"),

    /** 租户 */
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
