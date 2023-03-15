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

import com.art.common.core.model.PageResult;
import com.art.common.core.model.Result;
import com.art.system.api.tenant.dto.TenantPackageDTO;
import com.art.system.api.tenant.dto.TenantPackagePageDTO;
import com.art.system.service.TenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author fxz
 * @date 2022-10-01
 */
@Tag(name = "租户套餐管理")
@RestController
@RequestMapping("/tenant/package")
@RequiredArgsConstructor
public class TenantPackageController {

	private final TenantPackageService tenantPackageService;

	/**
	 * 保存租户套餐信息
	 */
	@Operation(summary = "保存租户套餐信息")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody TenantPackageDTO tenantPackageDTO) {
		return Result.success(tenantPackageService.addTenantPackage(tenantPackageDTO));
	}

	/**
	 * 更新租户套餐信息
	 */
	@Operation(summary = "更新租户套餐信息")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody TenantPackageDTO tenantPackageDTO) {
		return Result.success(tenantPackageService.updateTenantPackage(tenantPackageDTO));
	}

	/**
	 * 删除租户套餐信息
	 */
	@Operation(summary = "删除租户套餐信息")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(tenantPackageService.deleteTenantPackage(id));
	}

	/**
	 * 获取单条租户套餐信息
	 */
	@Operation(summary = "获取单条租户套餐信息")
	@GetMapping(value = "/findById")
	public Result<TenantPackageDTO> findById(Long id) {
		return Result.success(tenantPackageService.findById(id));
	}

	/**
	 * 获取全部租户套餐信息
	 */
	@Operation(summary = "获取全部租户套餐信息")
	@GetMapping(value = "/findAll")
	public Result<List<TenantPackageDTO>> findAll() {
		return Result.success(tenantPackageService.findAll());
	}

	/**
	 * 分页查询租户套餐信息
	 */
	@Operation(summary = "分页查询租户套餐信息")
	@GetMapping(value = "/page")
	public Result<PageResult<TenantPackageDTO>> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO) {
		return Result.success(PageResult.success(tenantPackageService.pageTenantPackage(tenantPackagePageDTO)));
	}

}
