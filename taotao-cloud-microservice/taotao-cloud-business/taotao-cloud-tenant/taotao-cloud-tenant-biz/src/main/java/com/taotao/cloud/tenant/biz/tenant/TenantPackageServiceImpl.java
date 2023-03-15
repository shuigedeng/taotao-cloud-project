/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.taotao.cloud.tenant.biz.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.art.common.core.constant.GlobalStatusEnum;
import com.art.common.core.exception.FxzException;
import com.art.system.api.tenant.dto.TenantPackageDTO;
import com.art.system.api.tenant.dto.TenantPackagePageDTO;
import com.art.system.core.convert.TenantPackageConvert;
import com.art.system.dao.dataobject.TenantDO;
import com.art.system.dao.dataobject.TenantPackageDO;
import com.art.system.manager.TenantManager;
import com.art.system.manager.TenantPackageManager;
import com.art.system.service.TenantPackageService;
import com.art.system.service.TenantService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @author fxz
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

	/**
	 * 添加
	 */
	@Override
	public Boolean addTenantPackage(TenantPackageDTO tenantPackageDTO) {
		return tenantPackageManager.addTenantPackage(tenantPackageDTO) > 0;
	}

	/**
	 * 更新租户套餐信息
	 */
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

	/**
	 * 删除租户套餐信息
	 */
	@Override
	public Boolean deleteTenantPackage(Long id) {
		// 校验套餐是否存在
		validTenantPackageExists(id);

		// 校验套餐是否正在使用
		validTenantPackageUsed(id);

		// 删除套餐信息
		return tenantPackageManager.deleteTenantPackageById(id) > 0;
	}

	/**
	 * 校验套餐信息
	 * @param packageId 套餐id
	 * @return 套餐信息
	 */
	@Override
	public TenantPackageDO validTenantPackage(Long packageId) {
		TenantPackageDO tenantPackageDO = tenantPackageManager.getTenantPackageById(packageId);
		if (Objects.isNull(tenantPackageDO)) {
			throw new FxzException("租户套餐不存在！");
		}
		else if (GlobalStatusEnum.DISABLE.getValue().equals(tenantPackageDO.getStatus())) {
			throw new FxzException("套餐未开启！");
		}

		return tenantPackageDO;
	}

	/**
	 * 校验套餐是否存在
	 * @param id 套餐id
	 * @return 租户套餐
	 */
	private TenantPackageDO validTenantPackageExists(Long id) {
		TenantPackageDO tenantPackageDO = tenantPackageManager.getTenantPackageById(id);
		if (Objects.isNull(tenantPackageDO)) {
			throw new FxzException("套餐信息不存在！更新失败！");
		}

		return tenantPackageDO;
	}

	private void validTenantPackageUsed(Long packageId) {
		Boolean res = tenantManager.validTenantPackageUsed(packageId);
		if (res) {
			throw new FxzException("套餐信息使用！");
		}
	}

	/**
	 * 分页查询租户套餐信息
	 */
	@Override
	public IPage<TenantPackageDTO> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO) {
		return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.pageTenantPackage(tenantPackagePageDTO));
	}

	/**
	 * 获取单条
	 */
	@Override
	public TenantPackageDTO findById(Long id) {
		return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.getTenantPackageById(id));
	}

	/**
	 * 获取全部
	 */
	@Override
	public List<TenantPackageDTO> findAll() {
		return TenantPackageConvert.INSTANCE.convert(tenantPackageManager.listTenantPackage());
	}

}
