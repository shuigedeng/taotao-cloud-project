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
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPageDTO;
import java.util.List;

/**
 * 租户表
 *
 */
public interface TenantService {

    /**
     * 校验租户信息是否合法
     *
     * @param id 租户id
     */
    void validTenant(Long id);

    /**
     * 获取所有租户id集合
     *
     * @return 所有租户id集合
     */
    List<Long> getTenantIds();

    /** 根据name查询租户Id */
    Long findTenantIdById(String name);

    /**
     * 保存租户信息
     *
     * @param tenant 租户信息
     */
    Boolean addSysTenant(TenantDTO tenant);

    /** 修改 */
    Boolean updateSysTenant(TenantDTO tenantDTO);

    /** 分页查询租户信息 */
    IPage<TenantDTO> pageSysTenant(TenantPageDTO pageDTO);

    /** 获取全部 */
    List<TenantDTO> findAll();

    /** 删除 */
    Boolean deleteSysTenant(Long id);

    /**
     * 更新指定租户的角色菜单信息
     *
     * @param id 租户id
     * @param menus 菜单信息
     */
    void updateTenantRoleMenu(Long id, List<String> menus);

    /** 校验租户下账号数量 */
    void validCount();

    /**
     * 根据id查询租户信息
     *
     * @param id 租户id
     * @return 租户信息
     */
    TenantDTO findById(Long id);
}
