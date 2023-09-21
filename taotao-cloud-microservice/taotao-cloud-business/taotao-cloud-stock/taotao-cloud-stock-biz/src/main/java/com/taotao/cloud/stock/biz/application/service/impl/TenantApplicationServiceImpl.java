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

package com.taotao.cloud.stock.biz.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户应用服务实现类
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Service
public class TenantApplicationServiceImpl implements TenantApplicationService {

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        TenantId tenantId = new TenantId(id);
        if (tenantId.isPlatformId()) {
            throw new RuntimeException("平台租户无法删除");
        }
        Tenant tenant = tenantRepository.find(tenantId);
        tenant.disable();
        tenantRepository.store(tenant);
    }
}
