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

package com.taotao.cloud.stock.api.common.util;

import lombok.experimental.UtilityClass;

/** 用户ID存储器 */
@UtilityClass
public class UserContext {

    /** 支持父子线程之间的数据传递 */
    private final ThreadLocal<String> THREAD_LOCAL_USER = new ThreadLocal<>();

    /**
     * 设置用户ID
     *
     * @param userId 租户ID
     */
    public void setUserId(String userId) {
        THREAD_LOCAL_USER.set(userId);
    }

    /**
     * 获取TTL中的用户ID
     *
     * @return String
     */
    public String getUserId() {
        return THREAD_LOCAL_USER.get();
    }

    /** 清除用户ID */
    public void clear() {
        THREAD_LOCAL_USER.remove();
    }
}
