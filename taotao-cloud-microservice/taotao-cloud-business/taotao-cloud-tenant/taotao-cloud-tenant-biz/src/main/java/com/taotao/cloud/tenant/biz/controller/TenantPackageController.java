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

package com.taotao.cloud.tenant.biz.controller;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPackagePageDTO;
import com.taotao.cloud.tenant.biz.service.TenantPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 10:58:30
 */
@Tag(name = "租户套餐管理")
@RestController
@RequestMapping("/tenant/package")
@RequiredArgsConstructor
public class TenantPackageController {

	private final TenantPackageService tenantPackageService;

	@Operation(summary = "保存租户套餐信息")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody TenantPackageDTO tenantPackageDTO) {
		return Result.success(tenantPackageService.addTenantPackage(tenantPackageDTO));
	}

	@Operation(summary = "更新租户套餐信息")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody TenantPackageDTO tenantPackageDTO) {
		return Result.success(tenantPackageService.updateTenantPackage(tenantPackageDTO));
	}

	@Operation(summary = "删除租户套餐信息")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.success(tenantPackageService.deleteTenantPackage(id));
	}

	@Operation(summary = "获取单条租户套餐信息")
	@GetMapping(value = "/findById")
	public Result<TenantPackageDTO> findById(Long id) {
		return Result.success(tenantPackageService.findById(id));
	}

	@Operation(summary = "获取全部租户套餐信息")
	@GetMapping(value = "/findAll")
	public Result<List<TenantPackageDTO>> findAll() {
		return Result.success(tenantPackageService.findAll());
	}

	@Operation(summary = "分页查询租户套餐信息")
	@GetMapping(value = "/page")
	public Result<PageResult<TenantPackageDTO>> pageTenantPackage(TenantPackagePageDTO tenantPackagePageDTO) {
		return Result.success(MpUtils.convertMybatisPage(
			tenantPackageService.pageTenantPackage(tenantPackagePageDTO), TenantPackageDTO.class));
	}
}
