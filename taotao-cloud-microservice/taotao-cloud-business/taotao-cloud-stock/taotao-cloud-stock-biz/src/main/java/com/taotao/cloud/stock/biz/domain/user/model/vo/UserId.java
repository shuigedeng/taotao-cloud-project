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

package com.taotao.cloud.stock.biz.domain.user.model.vo;

import lombok.Data;
import lombok.experimental.*;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户ID
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
@Data
public class UserId implements ValueObject<UserId> {

    /** 超级管理员角色 */
    public static final String SYS_ADMIN = "1";

    private String id;

    public UserId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isSysAdmin() {
        return SYS_ADMIN.equals(id);
    }

    @Override
    public boolean sameValueAs(UserId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
