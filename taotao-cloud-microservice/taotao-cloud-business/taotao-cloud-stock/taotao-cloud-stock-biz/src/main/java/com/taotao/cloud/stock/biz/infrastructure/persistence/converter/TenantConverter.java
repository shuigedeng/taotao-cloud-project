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

package com.taotao.cloud.stock.biz.infrastructure.persistence.converter;

/**
 * 租户Converter
 *
 * @author shuigedeng
 * @since 2021-02-15
 */
public class TenantConverter {

    public static Tenant toTenant(SysTenantDO sysTenantDO) {
        if (sysTenantDO == null) {
            return null;
        }
        TenantId tenantId = null;
        if (sysTenantDO.getId() != null) {
            tenantId = new TenantId(sysTenantDO.getId());
        }
        UserId creatorId = null;
        if (sysTenantDO.getCreatorId() != null) {
            creatorId = new UserId(sysTenantDO.getCreatorId());
        }
        Tenant tenant = new Tenant(
                tenantId,
                new TenantCode(sysTenantDO.getTenantCode()),
                new TenantName(sysTenantDO.getTenantName()),
                StatusEnum.getStatusEnum(sysTenantDO.getStatus()),
                creatorId);
        return tenant;
    }

    public static SysTenantDO getSysTenantDO(Tenant tenant) {
        if (tenant == null) {
            throw new RuntimeException("租户不存在");
        }
        SysTenantDO sysTenantDO = new SysTenantDO();
        sysTenantDO.setId(
                tenant.getTenantId() == null ? null : tenant.getTenantId().getId());
        sysTenantDO.setTenantCode(
                tenant.getTenantCode() == null ? null : tenant.getTenantCode().getCode());
        sysTenantDO.setTenantName(
                tenant.getTenantName() == null ? null : tenant.getTenantName().getName());
        sysTenantDO.setStatus(
                tenant.getStatus() == null ? null : tenant.getStatus().getValue());
        sysTenantDO.setCreatorId(
                tenant.getCreatorId() == null ? null : tenant.getCreatorId().getId());
        return sysTenantDO;
    }
}
