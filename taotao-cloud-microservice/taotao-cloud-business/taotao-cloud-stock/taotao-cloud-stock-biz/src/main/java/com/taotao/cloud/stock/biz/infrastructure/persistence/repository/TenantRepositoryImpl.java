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

package com.taotao.cloud.stock.biz.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户-Repository实现类
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Repository
public class TenantRepositoryImpl extends ServiceImpl<SysTenantMapper, SysTenantDO>
        implements TenantRepository, IService<SysTenantDO> {

    @Override
    public Tenant find(TenantId tenantId) {
        SysTenantDO sysTenantDO = this.getById(tenantId.getId());
        if (sysTenantDO == null) {
            return null;
        }
        Tenant tenant = TenantConverter.toTenant(sysTenantDO);
        return tenant;
    }

    @Override
    public Tenant find(TenantName tenantName) {
        SysTenantDO sysTenantDO = this.getOne(new QueryWrapper<SysTenantDO>().eq("tenant_name", tenantName.getName()));
        if (sysTenantDO == null) {
            return null;
        }
        Tenant tenant = TenantConverter.toTenant(sysTenantDO);
        return tenant;
    }

    @Override
    public Tenant find(TenantCode tenantCode) {
        SysTenantDO sysTenantDO = this.getOne(new QueryWrapper<SysTenantDO>().eq("tenant_code", tenantCode.getCode()));
        if (sysTenantDO == null) {
            return null;
        }
        Tenant tenant = TenantConverter.toTenant(sysTenantDO);
        return tenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantId store(Tenant tenant) {
        SysTenantDO sysTenantDO = TenantConverter.getSysTenantDO(tenant);
        this.saveOrUpdate(sysTenantDO);
        if (TenantContext.getTenantId() == null) {
            TenantContext.setTenantId(sysTenantDO.getId());
        }
        return new TenantId(sysTenantDO.getId());
    }
}
