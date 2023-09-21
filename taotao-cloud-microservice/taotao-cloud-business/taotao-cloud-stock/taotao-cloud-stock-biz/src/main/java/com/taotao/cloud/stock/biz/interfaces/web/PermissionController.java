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
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 权限Controller
 *
 * @author shuigedeng
 * @since 2021-02-16
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionApplicationService permissionApplicationService;

    @Autowired
    private PermissionQueryService permissionQueryService;

    /** 导航菜单 */
    @ApiOperation("导航菜单")
    @GetMapping("/nav")
    public Result nav() {
        List<PermissionDTO> menuList = permissionQueryService.getUserMenuTree(RequestUtils.getUserId());
        Set<String> permissions = permissionQueryService.getPermissionCodes(RequestUtils.getUserId());
        return Result.ok().put("menuList", menuList).put("permissions", permissions);
    }

    /** 所有权限列表 */
    @ApiOperation("所有权限列表")
    @GetMapping("/list")
    public Result list() {
        List<PermissionDTO> permissionList = permissionQueryService.listAllPermission();
        return Result.ok().put("permissionList", permissionList);
    }

    /** 选择菜单 */
    @ApiOperation("选择菜单")
    @GetMapping("/selectMenu")
    public Result selectMenu() {
        List<PermissionDTO> menuList = permissionQueryService.listAllMenu();
        return Result.ok().put("menuList", menuList);
    }

    /** 权限信息 */
    @ApiOperation("权限信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:permission:info')")
    public Result info(@PathVariable("id") String id) {
        PermissionDTO permission = permissionQueryService.getById(id);
        return Result.ok().put("permission", permission);
    }

    /** 保存权限 */
    @ApiOperation("保存权限")
    @SysLog("保存权限")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:permission:save')")
    public Result save(@RequestBody PermissionCommand permissionCommand) {
        ValidatorUtils.validateEntity(permissionCommand, AddGroup.class);
        permissionApplicationService.saveOrUpdate(permissionCommand);
        return Result.ok();
    }

    /** 修改权限 */
    @ApiOperation("修改权限")
    @SysLog("修改权限")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:permission:update')")
    public Result update(@RequestBody PermissionCommand permissionCommand) {
        ValidatorUtils.validateEntity(permissionCommand, UpdateGroup.class);
        permissionApplicationService.saveOrUpdate(permissionCommand);
        return Result.ok();
    }

    /** 删除权限 */
    @ApiOperation("删除权限")
    @SysLog("删除权限")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Result delete(@PathVariable("id") String id) {
        permissionApplicationService.delete(id);
        return Result.ok();
    }

    /** 禁用权限 */
    @ApiOperation("禁用权限")
    @SysLog("禁用权限")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:permission:disable')")
    public Result disable(@PathVariable("id") String id) {
        permissionApplicationService.disable(id);
        return Result.ok();
    }
}
