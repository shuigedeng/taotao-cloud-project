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

/** 多租户 tenant_id存储器 */
@UtilityClass
public class TenantContext {

    /** 支持父子线程之间的数据传递 */
    private final ThreadLocal<String> THREAD_LOCAL_TENANT = new ThreadLocal<>();

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    /**
     * 获取TTL中的租户ID
     *
     * @return String
     */
    public String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    /** 清除tenantId */
    public void clear() {
        THREAD_LOCAL_TENANT.remove();
    }
}
