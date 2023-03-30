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

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPageDTO;
import com.taotao.cloud.tenant.biz.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户表
 *
 * @author
 * @date 2022-10-01
 */
@Tag(name = "租户管理")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    /**
     * 获取所有租户id集合
     *
     * @return 所有租户id集合
     */
    @Operation(summary = "获取所有租户id集合")
    @GetMapping(value = "/getTenantIds")
    public List<Long> getTenantIds() {
        return tenantService.getTenantIds();
    }

    /**
     * 校验租户信息是否合法
     *
     * @param id 租户id
     */
    @Operation(summary = "校验租户信息是否合法")
    @GetMapping(value = "/validTenant/{id}")
    public void validTenant(@PathVariable("id") Long id) {
        tenantService.validTenant(id);
    }

    /**
     * 保存租户信息
     *
     * @param tenant 租户信息
     */
    @Operation(summary = "保存租户信息")
    @PostMapping(value = "/add")
    public Result<Boolean> add(@RequestBody TenantDTO tenant) {
        return Result.success(tenantService.addSysTenant(tenant));
    }

    /** 修改租户信息 */
    @Operation(summary = "修改租户信息")
    @PostMapping(value = "/update")
    public Result<Boolean> update(@RequestBody TenantDTO tenantDTO) {
        return Result.success(tenantService.updateSysTenant(tenantDTO));
    }

    /** 删除租户信息 */
    @Operation(summary = "删除租户信息")
    @DeleteMapping(value = "/delete")
    public Result<Boolean> delete(Long id) {
        return Result.success(tenantService.deleteSysTenant(id));
    }

    /** 根据id查询租户信息 */
    @Operation(summary = "根据id查询租户信息")
    @GetMapping(value = "/findById")
    public Result<TenantDTO> findById(Long id) {
        return Result.success(tenantService.findById(id));
    }

    /** 根据name查询租户Id */
    @Operation(summary = "根据name查询租户Id")
    @GetMapping(value = "/findIdByName")
    public Result<Long> findTenantIdById(String name) {
        return Result.success(tenantService.findTenantIdById(name));
    }

    /** 获取全部租户信息 */
    @Operation(summary = "获取全部租户信息")
    @GetMapping(value = "/findAll")
    public Result<List<TenantDTO>> findAll() {
        return Result.success(tenantService.findAll());
    }

    /** 分页查询租户列表 */
    @Operation(summary = "分页查询租户列表")
    @GetMapping(value = "/page")
    public Result<PageResult<TenantDTO>> pageSysTenant(TenantPageDTO pageDTO) {
        return Result.success(
                PageResult.convertMybatisPage(
                        tenantService.pageSysTenant(pageDTO), TenantDTO.class));
    }
}
