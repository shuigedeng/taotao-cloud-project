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

package com.taotao.cloud.tenant.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPackagePageDTO;
import com.taotao.cloud.tenant.biz.entity.TenantPackage;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author
 * @since 2022-10-01
 */
public interface TenantPackageService {

    /** 添加 */
    Boolean addTenantPackage(TenantPackageDTO tenantPackageDTO);

    /** 更新租户套餐信息 */
    Boolean updateTenantPackage(TenantPackageDTO tenantPackageDTO);

    /** 分页查询租户套餐信息 */
    IPage<TenantPackageDTO> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO);

    /** 获取单条 */
    TenantPackageDTO findById(Long id);

    /** 获取全部 */
    List<TenantPackageDTO> findAll();

    /** 删除 */
    Boolean deleteTenantPackage(Long id);

    /**
     * 校验套餐信息
     *
     * @param packageId 套餐id
     * @return 套餐信息
     */
    TenantPackage validTenantPackage(Long packageId);
}
