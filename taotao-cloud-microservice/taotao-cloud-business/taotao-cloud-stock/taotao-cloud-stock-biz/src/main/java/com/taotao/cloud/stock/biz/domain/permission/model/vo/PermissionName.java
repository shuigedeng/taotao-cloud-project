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

package com.taotao.cloud.stock.biz.domain.permission.model.vo;

import com.taotao.cloud.stock.api.common.domain.ValueObject;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 权限名称
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
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
