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

package com.taotao.cloud.stock.biz.interfaces.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 角色Controller
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleQueryService roleQueryService;

    @Autowired
    private RoleApplicationService roleApplicationService;

    /** 角色分页查询 */
    @ApiOperation("角色分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = roleQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /** 角色列表 */
    @ApiOperation("角色列表")
    @GetMapping("/select")
    @PreAuthorize("hasAuthority('sys:role:select')")
    public Result select() {
        List<RoleDTO> list = roleQueryService.listAll();
        return Result.ok().put("list", list);
    }

    /** 角色信息 */
    @ApiOperation("角色信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result info(@PathVariable("id") String id) {
        RoleDTO role = roleQueryService.getById(id);
        return Result.ok().put("role", role);
    }

    /** 保存角色 */
    @ApiOperation("保存角色")
    @SysLog("保存角色")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@RequestBody RoleCommand roleCommand) {
        ValidatorUtils.validateEntity(roleCommand);
        roleApplicationService.saveOrUpdate(roleCommand);
        return Result.ok();
    }

    /** 修改角色 */
    @ApiOperation("修改角色")
    @SysLog("修改角色")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@RequestBody RoleCommand roleCommand) {
        ValidatorUtils.validateEntity(roleCommand);
        roleApplicationService.saveOrUpdate(roleCommand);
        return Result.ok();
    }

    /** 删除角色 */
    @ApiOperation("删除角色")
    @SysLog("删除角色")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result delete(@RequestBody String[] roleIds) {
        roleApplicationService.deleteBatch(Arrays.asList(roleIds));
        return Result.ok();
    }

    /** 禁用角色 */
    @ApiOperation("禁用角色")
    @SysLog("禁用角色")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:role:disable')")
    public Result disable(@PathVariable("id") String id) {
        roleApplicationService.disable(id);
        return Result.ok();
    }
}
