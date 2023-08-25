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
import com.taotao.cloud.common.enums.GlobalStatusEnum;
import com.taotao.cloud.sys.api.feign.IFeignRoleApi;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPageDTO;
import com.taotao.cloud.tenant.biz.convert.TenantConvert;
import com.taotao.cloud.tenant.biz.dao.TenantManager;
import com.taotao.cloud.tenant.biz.entity.Tenant;
import com.taotao.cloud.tenant.biz.entity.TenantPackage;
import com.taotao.cloud.tenant.biz.service.TenantPackageService;
import com.taotao.cloud.tenant.biz.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 租户表
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    @Resource
    private TenantPackageService tenantPackageService;

    @Resource
    private TenantManager tenantManager;

    @Resource
    private IFeignUserApi userApi;

    @Resource
    private IFeignRoleApi roleApi;

    @Override
    public void validTenant(Long id) {
        Tenant tenant = tenantManager.getTenantById(id);

        if (Objects.isNull(tenant)) {
            throw new RuntimeException("租户信息不存在!");
        }
        if (tenant.getStatus().equals(GlobalStatusEnum.DISABLE.getValue())) {
            throw new RuntimeException(String.format("租户未开启:%s", tenant.getName()));
        }
        if (LocalDateTime.now().isAfter(tenant.getExpireTime())) {
            throw new RuntimeException("租户已经过期！");
        }
    }

    @Override
    public List<Long> getTenantIds() {
        return tenantManager.listTenant().stream().map(Tenant::getId).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addSysTenant(TenantDTO tenant) {
        // 检查套餐信息
        //TenantPackage tenantPackage = tenantPackageService.validTenantPackage(tenant.getPackageId());

        // 保存租户信息
        Long tenantId = tenantManager.addTenant(tenant);

        return Boolean.TRUE;

        //		TenantUtils.run(tenantId, () -> {
        //			// 根据套餐信息为新租户新建一个角色
        //			Long roleId = createRole(tenantPackageDO);
        //			// 为新租户创建一个默认账号
        //			Long userId = createUser(roleId, tenant);
        //			// 更新租户管理员id
        //			tenantManager.updateTenantAdmin(tenant.getId(), userId);
        //		});

    }

    /**
     * 根据角色id与租户信息创建一个默认账号
     *
     * @param roleId 角色id
     * @param tenant 租户信息
     * @return userId
     */
    private Long createUser(Long roleId, TenantDTO tenant) {
        // 创建用户
        //		SystemUserDTO systemUserDO = new SystemUserDTO().setUsername(tenant.getUsername())
        //			.setPassword(tenant.getPassword())
        //			.setMobile(tenant.getTenantAdminMobile())
        //			.setUsername(tenant.getTenantAdminName())
        //			.setRoleId(String.valueOf(roleId));
        //
        //		return userApi.createUser(systemUserDO).getUserId();
        return 0L;
    }

    /**
     * 根据套餐生成一个角色信息
     *
     * @param tenantPackage 租户套餐
     * @return 角色信息
     */
    private Long createRole(TenantPackage tenantPackage) {
        // 生成租户管理员角色角色
        //		RoleDTO roleDTO = new RoleDTO().setRoleName(RoleAdminEnum.TENANT_ADMIN.getDescription())
        //			.setCode(RoleAdminEnum.TENANT_ADMIN.getType())
        //			.setRemark("系统生成租户管理员角色")
        //			.setMenuId(tenantPackageDO.getMenuIds())
        //			.setDataScope(DataScopeEnum.ALL.getScope());
        //
        //		return roleApi.addRole(roleDTO).getRoleId();
        return 0L;
    }

    /**
     * 根据name查询租户信息
     */
    @Override
    public Long findTenantIdById(String name) {
        Tenant tenant = tenantManager.getTenantByName(name);
        if (Objects.isNull(tenant)) {
            return null;
        }

        return tenant.getId();
    }

    /**
     * 删除租户
     */
    @Override
    public Boolean deleteSysTenant(Long id) {
        if (isSystemTenant(id)) {
            throw new RuntimeException("系统内置租户，不允许删除");
        }

        return tenantManager.deleteTenantById(id) > 0;
    }

    /**
     * 更新指定租户的角色菜单信息
     *
     * @param id    租户id
     * @param menus 菜单信息
     */
    @Override
    public void updateTenantRoleMenu(Long id, List<String> menus) {
        //		TenantUtils.run(id, () -> {
        //			// 本租户下的所有角色
        //			List<RoleDTO> roleDTOList = roleService.getAllRole();
        //
        //			roleDTOList.forEach(r -> {
        //				if (Objects.equals(r.getCode(), RoleAdminEnum.TENANT_ADMIN.getType())) {
        //					// 租户管理员 则直接赋值新菜单
        //					r.setMenuId(String.join(StrPool.COMMA, menus));
        //				} else {
        //					// 非租户管理员 则原菜单和现菜单取交集
        //					r.setMenuId(String.join(StrPool.COMMA,
        //						CollUtil.intersectionDistinct(menus,
        // Arrays.asList(r.getMenuId().split(StrPool.COMMA)))));
        //				}
        //
        //				// 更新角色信息
        //				roleApi.editRole(r);
        //			});
        //		});
    }

    @Override
    public void validCount() {
        //		long count = userApi.count();
        //		Long tenantId = TenantContextHolder.getTenantId();
        //		TenantDO tenantDO = tenantManager.getTenantById(tenantId);
        //
        //		if (Objects.isNull(tenantDO) || count > tenantDO.getAccountCount()) {
        //			throw new RuntimeException("租户账号数量超过额度！");
        //		}
    }

    @Override
    public TenantDTO findById(Long id) {
        return TenantConvert.INSTANCE.convert(tenantManager.getTenantById(id));
    }

    @Override
    public Boolean updateSysTenant(TenantDTO tenantDTO) {
        return tenantManager.updateTenant(tenantDTO) > 0;
    }

    @Override
    public IPage<TenantDTO> pageSysTenant(TenantPageDTO pageDTO) {
        return TenantConvert.INSTANCE.convert(tenantManager.pageTenant(pageDTO));
    }

    @Override
    public List<TenantDTO> findAll() {
        return TenantConvert.INSTANCE.convert(tenantManager.listTenant());
    }

    /**
     * 校验租户是否是系统租户
     *
     * @param id 租户id 我们任务租户id为0时为系统内置租户 不允许删除
     * @return 是否是系统租户
     */
    private boolean isSystemTenant(Long id) {
        return Objects.equals(id, Tenant.PACKAGE_ID_SYSTEM);
    }
}
