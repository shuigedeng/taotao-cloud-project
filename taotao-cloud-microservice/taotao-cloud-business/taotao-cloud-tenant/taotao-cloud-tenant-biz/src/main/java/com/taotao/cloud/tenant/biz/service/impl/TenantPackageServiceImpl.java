/*
 * COPYRIGHT (C) 2022 Art AUTHORS(cloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tenant.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.GlobalStatusEnum;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPackagePageDTO;
import com.taotao.cloud.tenant.biz.convert.TenantPackageConvert;
import com.taotao.cloud.tenant.biz.dao.TenantManager;
import com.taotao.cloud.tenant.biz.dao.TenantPackageManager;
import com.taotao.cloud.tenant.biz.entity.TenantDO;
import com.taotao.cloud.tenant.biz.entity.TenantPackageDO;
import com.taotao.cloud.tenant.biz.service.TenantPackageService;
import com.taotao.cloud.tenant.biz.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 租户套餐表
 *
 * @author 
 * @date 2022-10-01
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
		TenantPackageDO packageExists = validTenantPackageExists(tenantPackageDTO.getId());

		// 更新套餐信息
		tenantPackageManager.updateTenantPackageById(tenantPackageDTO);

		// 租户原菜单信息
		String[] newMenus = tenantPackageDTO.getMenuIds().split(StrPool.COMMA);
		// 更新后的菜单信息
		String[] oldMenus = packageExists.getMenuIds().split(StrPool.COMMA);

		// 菜单信息变化 则更新租户下的角色菜单信息
		if (!CollUtil.isEqualList(Arrays.asList(newMenus), Arrays.asList(oldMenus))) {
			// 本套餐下的所有租户
			List<TenantDO> tenantDOList = tenantManager.getTenantListByPackageId(tenantPackageDTO.getId());

			// 遍历所有租户 更新租户下的角色菜单信息
			tenantDOList.forEach(t -> tenantService.updateTenantRoleMenu(t.getId(), Arrays.asList(newMenus)));
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
	public TenantPackageDO validTenantPackage(Long packageId) {
		TenantPackageDO tenantPackageDO = tenantPackageManager.getTenantPackageById(packageId);
		if (Objects.isNull(tenantPackageDO)) {
			throw new RuntimeException("租户套餐不存在！");
		} else if (GlobalStatusEnum.DISABLE.getValue().equals(tenantPackageDO.getStatus())) {
			throw new RuntimeException("套餐未开启！");
		}

		return tenantPackageDO;
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
	private TenantPackageDO validTenantPackageExists(Long id) {
		TenantPackageDO tenantPackageDO = tenantPackageManager.getTenantPackageById(id);
		if (Objects.isNull(tenantPackageDO)) {
			throw new RuntimeException("套餐信息不存在！更新失败！");
		}

		return tenantPackageDO;
	}

	private void validTenantPackageUsed(Long packageId) {
		Boolean res = tenantManager.validTenantPackageUsed(packageId);
		if (res) {
			throw new RuntimeException("套餐信息使用！");
		}
	}
}
