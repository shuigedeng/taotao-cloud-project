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

package com.taotao.cloud.tenant.biz.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPackagePageDTO;
import com.taotao.cloud.tenant.biz.entity.TenantPackageDO;
import com.taotao.cloud.tenant.biz.convert.TenantPackageConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author 
 * @version 0.0.1
 * @date 2022/11/26 15:27
 */
@RequiredArgsConstructor
@Component
public class TenantPackageManager {

	private final TenantPackageMapper tenantPackageMapper;

	public Integer addTenantPackage(TenantPackageDTO tenantPackageDTO) {
		return tenantPackageMapper.insert(TenantPackageConvert.INSTANCE.convert(tenantPackageDTO));
	}

	public TenantPackageDO getTenantPackageById(Long id) {
		return tenantPackageMapper.selectById(id);
	}

	public void updateTenantPackageById(TenantPackageDTO tenantPackageDTO) {
		tenantPackageMapper.updateById(TenantPackageConvert.INSTANCE.convert(tenantPackageDTO));
	}

	public Integer deleteTenantPackageById(Long id) {
		return tenantPackageMapper.deleteById(id);
	}

	public List<TenantPackageDO> listTenantPackage() {
		return tenantPackageMapper.selectList(Wrappers.emptyWrapper());
	}

	public Page<TenantPackageDO> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO) {
		LambdaQueryWrapper<TenantPackageDO> wrapper = Wrappers.<TenantPackageDO>lambdaQuery()
			.eq(Objects.nonNull(tenantPackagePageDTO.getId()), TenantPackageDO::getId, tenantPackagePageDTO.getId())
			.eq(Objects.nonNull(tenantPackagePageDTO.getStatus()), TenantPackageDO::getStatus,
					tenantPackagePageDTO.getStatus())
			.like(StrUtil.isNotBlank(tenantPackagePageDTO.getName()), TenantPackageDO::getName,
					tenantPackagePageDTO.getName());
		return tenantPackageMapper
			.selectPage(Page.of(tenantPackagePageDTO.getCurrentPage(), tenantPackagePageDTO.getPageSize()), wrapper);
	}

}
