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

package com.taotao.cloud.tenant.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.GlobalStatusEnum;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPackagePageDTO;
import com.taotao.cloud.tenant.biz.convert.TenantPackageConvert;
import com.taotao.cloud.tenant.biz.dao.TenantManager;
import com.taotao.cloud.tenant.biz.dao.TenantPackageManager;
import com.taotao.cloud.tenant.biz.entity.Tenant;
import com.taotao.cloud.tenant.biz.entity.TenantPackage;
import com.taotao.cloud.tenant.biz.service.TenantPackageService;
import com.taotao.cloud.tenant.biz.service.TenantService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 租户套餐表
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantPackageServiceImpl implements TenantPackageService {

    @Resource
    private TenantService tenantService;

    private final TenantManager tenantManager;

    private final TenantPackageManager tenantPackageManager;

    @Override
    public Boolean addTenantPackage(TenantPackageDTO tenantPackageDTO) {
        return tenantPackageManager.addTenantPackage(tenantPackageDTO) > 0;
    }

    @Override
    public Boolean updateTenantPackage(TenantPackageDTO tenantPackageDTO) {
        // 校验套餐是否存在
        TenantPackage packageExists = validTenantPackageExists(tenantPackageDTO.getId());

        // 更新套餐信息
        tenantPackageManager.updateTenantPackageById(tenantPackageDTO);

        // 租户原菜单信息
        String[] newMenus = tenantPackageDTO.getMenuIds().split(StrPool.COMMA);
        // 更新后的菜单信息
        String[] oldMenus = packageExists.getMenuIds().split(StrPool.COMMA);

        // 菜单信息变化 则更新租户下的角色菜单信息
        if (!CollUtil.isEqualList(Arrays.asList(newMenus), Arrays.asList(oldMenus))) {
            // 本套餐下的所有租户
            List<Tenant> tenantList = tenantManager.getTenantListByPackageId(tenantPackageDTO.getId());

            // 遍历所有租户 更新租户下的角色菜单信息
            tenantList.forEach(t -> tenantService.updateTenantRoleMenu(t.getId(), Arrays.asList(newMenus)));
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteTenantPackage(Long id) {
        // 校验套餐是否存在
        validTenantPackageExists(id);

        // 校验套餐是否正在使用
        validTenantPackageUsed(id);

        // 删除套餐信息
        return tenantPackageManager.deleteTenantPackageById(id) > 0;
    }

    @Override
    public TenantPackage validTenantPackage(Long packageId) {
        TenantPackage tenantPackage = tenantPackageManager.getTenantPackageById(packageId);
        if (Objects.isNull(tenantPackage)) {
            throw new RuntimeException("租户套餐不存在！");
        } else if (GlobalStatusEnum.DISABLE.getValue().equals(tenantPackage.getStatus())) {
            throw new RuntimeException("套餐未开启！");
        }

        return tenantPackage;
    }

    @Override
    public IPage<TenantPackageDTO> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO) {
        return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.pageTenantPackage(tenantPackagePageDTO));
    }

    @Override
    public TenantPackageDTO findById(Long id) {
        return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.getTenantPackageById(id));
    }

    @Override
    public List<TenantPackageDTO> findAll() {
        return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.listTenantPackage());
    }

    /**
     * 校验套餐是否存在
     *
     * @param id 套餐id
     * @return 租户套餐
     */
    private TenantPackage validTenantPackageExists(Long id) {
        TenantPackage tenantPackage = tenantPackageManager.getTenantPackageById(id);
        if (Objects.isNull(tenantPackage)) {
            throw new RuntimeException("套餐信息不存在！更新失败！");
        }

        return tenantPackage;
    }

    private void validTenantPackageUsed(Long packageId) {
        Boolean res = tenantManager.validTenantPackageUsed(packageId);
        if (res) {
            throw new RuntimeException("套餐信息使用！");
        }
    }
}
