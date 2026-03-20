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

package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.boot.webagg.controller.BusinessController;
import com.taotao.cloud.tenant.biz.application.dto.command.TenantAddCommand;
import com.taotao.cloud.tenant.biz.application.service.commad.TenantCommandService;
import com.taotao.cloud.tenant.biz.application.service.query.TenantQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户表
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 10:58:13
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/manager/sys/user")
@Tag(name = "平台管理端-用户API", description = "平台管理端-用户API")
public class TenantController extends BusinessController {

	private final TenantQueryService tenantQueryService;
	private final TenantCommandService tenantCommandService;


	@Operation(summary = "插入数据", description = "插入数据")
	@PostMapping("/insert")
	@NotAuth
	public Result<Void> insert( @Valid @RequestBody TenantAddCommand tenantAddCommand ) {
		tenantCommandService.insert(tenantAddCommand);

		return Result.success((Void) null);
	}

	@Operation(summary = "查询数据", description = "查询数据")
	@PostMapping("/select")
	@NotAuth
	public Result<Void> select( @Valid @RequestBody TenantAddCommand tenantAddCommand ) {
		tenantCommandService.select();

		return Result.success((Void) null);
	}
//	@Operation(summary = "获取所有租户id集合")
//	@GetMapping(value = "/getTenantIds")
//	public List<Long> getTenantIds() {
//		return tenantService.getTenantIds();
//	}
//
//	@Operation(summary = "校验租户信息是否合法")
//	@GetMapping(value = "/validTenant/{id}")
//	public void validTenant(@PathVariable("id") Long id) {
//		tenantService.validTenant(id);
//	}
//
//	@Operation(summary = "保存租户信息")
//	@PostMapping(value = "/add")
//	public Result<Boolean> add(@RequestBody TenantDTO tenant) {
//		return Result.success(tenantService.addSysTenant(tenant));
//	}
//
//	@Operation(summary = "修改租户信息")
//	@PostMapping(value = "/update")
//	public Result<Boolean> update(@RequestBody TenantDTO tenantDTO) {
//		return Result.success(tenantService.updateSysTenant(tenantDTO));
//	}
//
//	@Operation(summary = "删除租户信息")
//	@PostMapping(value = "/delete")
//	public Result<Boolean> delete(Long id) {
//		return Result.success(tenantService.deleteSysTenant(id));
//	}
//
//	@Operation(summary = "根据id查询租户信息")
//	@GetMapping(value = "/findById")
//	public Result<TenantDTO> findById(Long id) {
//		return Result.success(tenantService.findById(id));
//	}
//
//	@Operation(summary = "根据name查询租户Id")
//	@GetMapping(value = "/findIdByName")
//	public Result<Long> findTenantIdById(String name) {
//		return Result.success(tenantService.findTenantIdById(name));
//	}
//
//	@Operation(summary = "获取全部租户信息")
//	@GetMapping(value = "/findAll")
//	public Result<List<TenantDTO>> findAll() {
//		return Result.success(tenantService.findAll());
//	}
//
//	@Operation(summary = "分页查询租户列表")
//	@GetMapping(value = "/page")
//	public Result<PageResult<TenantDTO>> pageSysTenant(TenantPageDTO pageDTO) {
//		return Result.success(MpUtils.convertMybatisPage(tenantService.pageSysTenant(pageDTO), TenantDTO.class));
//	}
}
