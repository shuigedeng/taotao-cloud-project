package com.taotao.cloud.stock.biz.interfaces.web;

import com.xtoon.cloud.common.core.util.RequestUtils;
import com.xtoon.cloud.common.log.SysLog;
import com.xtoon.cloud.common.mybatis.constant.PageConstant;
import com.xtoon.cloud.common.mybatis.util.Page;
import com.xtoon.cloud.common.web.util.Result;
import com.xtoon.cloud.common.web.util.validator.ValidatorUtils;
import com.xtoon.cloud.common.web.util.validator.group.AddGroup;
import com.xtoon.cloud.common.web.util.validator.group.UpdateGroup;
import com.xtoon.cloud.sys.application.UserApplicationService;
import com.xtoon.cloud.sys.application.UserQueryService;
import com.xtoon.cloud.sys.application.command.PasswordCommand;
import com.xtoon.cloud.sys.application.command.UserCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author haoxin
 * @date 2021-02-20
 **/
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserQueryService userQueryService;

    /**
     * 用户分页查询
     */
    @ApiOperation("用户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = userQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /**
     * 获取登录的用户信息
     */
    @ApiOperation("获取登录的用户信息")
    @GetMapping("/info")
    public Result info() {
        return Result.ok().put("user", userQueryService.find(RequestUtils.getUserId()));
    }

    /**
     * 修改登录用户密码
     */
    @ApiOperation("修改密码")
    @SysLog("修改密码")
    @PostMapping("/password")
    public Result changePassword(@RequestBody PasswordCommand passwordCommand) {
        ValidatorUtils.validateEntity(passwordCommand);
        passwordCommand.setUserId(RequestUtils.getUserId());
        userApplicationService.changePassword(passwordCommand);
        return Result.ok();
    }

    /**
     * 用户信息
     */
    @ApiOperation("用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result info(@PathVariable("id") String id) {
        return Result.ok().put("user", userQueryService.find(id));
    }

    /**
     * 保存用户
     */
    @ApiOperation("保存用户")
    @SysLog("保存用户")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, AddGroup.class);
        userApplicationService.save(userCommand);
        return Result.ok();
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @SysLog("修改用户")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@RequestBody UserCommand userCommand) {
        ValidatorUtils.validateEntity(userCommand, UpdateGroup.class);
        userApplicationService.update(userCommand);
        return Result.ok();
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @SysLog("删除用户")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result delete(@RequestBody String[] userIds) {
        userApplicationService.deleteBatch(Arrays.asList(userIds));
        return Result.ok();
    }

    /**
     * 禁用用户
     */
    @ApiOperation("禁用用户")
    @SysLog("禁用用户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:user:disable')")
    public Result disable(@PathVariable("id") String id) {
        userApplicationService.disable(id);
        return Result.ok();
    }
}
