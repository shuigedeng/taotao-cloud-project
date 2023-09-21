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
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 *
 * @author shuigedeng
 * @since 2021-02-20
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserQueryService userQueryService;

    /** 用户分页查询 */
    @ApiOperation("用户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = userQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /** 获取登录的用户信息 */
    @ApiOperation("获取登录的用户信息")
    @GetMapping("/info")
    public Result info() {
        return Result.ok().put("user", userQueryService.find(RequestUtils.getUserId()));
    }

    /** 修改登录用户密码 */
    @ApiOperation("修改密码")
    @SysLog("修改密码")
    @PostMapping("/password")
    public Result changePassword(@RequestBody PasswordCommand passwordCommand) {
        ValidatorUtils.validateEntity(passwordCommand);
        passwordCommand.setUserId(RequestUtils.getUserId());
        userApplicationService.changePassword(passwordCommand);
        return Result.ok();
    }

    /** 用户信息 */
    @ApiOperation("用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result info(@PathVariable("id") String id) {
        return Result.ok().put("user", userQueryService.find(id));
    }

    /** 保存用户 */
    @ApiOperation("保存用户")
    @SysLog("保存用户")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, AddGroup.class);
        userApplicationService.save(userCommand);
        return Result.ok();
    }

    /** 修改用户 */
    @ApiOperation("修改用户")
    @SysLog("修改用户")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, UpdateGroup.class);
        userApplicationService.update(userCommand);
        return Result.ok();
    }

    /** 删除用户 */
    @ApiOperation("删除用户")
    @SysLog("删除用户")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result delete(@RequestBody String[] userIds) {
        userApplicationService.deleteBatch(Arrays.asList(userIds));
        return Result.ok();
    }

    /** 禁用用户 */
    @ApiOperation("禁用用户")
    @SysLog("禁用用户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:user:disable')")
    public Result disable(@PathVariable("id") String id) {
        userApplicationService.disable(id);
        return Result.ok();
    }
}
