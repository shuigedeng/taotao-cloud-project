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

package com.taotao.cloud.tenant.api.feign.fallback;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.tenant.api.feign.TenantServiceApi;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignTenantFallbackImpl implements FallbackFactory<TenantServiceApi> {

    @Override
    public TenantServiceApi create(Throwable throwable) {
        return new TenantServiceApi() {

            @Override
            public void validTenant(Long id) {
                LogUtils.error("taotao-cloud-tenant  TenantServiceApi validTenant feign调用失败 ===============");
            }

            @Override
            public String addTenantWithTestSeata(TenantDTO tenantDTO) {
                LogUtils.error("taotao-cloud-tenant  TenantServiceApi addTenantWithTestSeata feign调用失败 ===============");
                return null;
            }
        };
    }
}
