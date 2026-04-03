package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.tenant.biz.application.dto.SysUserDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysUserQuery;
import com.taotao.cloud.tenant.biz.application.dto.UserOrgBindDTO;
import import com.taotao.cloud.tenant.biz.application.service.service.ISysUserService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理Controller
 */
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysUserController {

    private final ISysUserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public Result<IPage<SysUser>> page( SysUserQuery query) {
        IPage<SysUser> page = userService.selectUserPage(query);
        return Result.success(page);
    }

    /**
     * 根据ID查询用户详情
     */
    @PostMapping("/getById")
    public Result<SysUser> getById(@RequestParam Long id) {
        SysUser user = userService.selectUserById(id);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysUserDTO dto) {
        boolean result = userService.insertUser(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改用户
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysUserDTO dto) {
        boolean result = userService.updateUser(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除用户
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = userService.deleteUserById(id);
        return result ? Result.success() : Result.error("删除失败");
    }
    
    @PostMapping("/doUntieDisable")
    public Result<Void> doUntieDisable(@RequestParam Long id) {
        userService.doUntieDisable(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] ids) {
        boolean result = userService.deleteUserByIds(ids);
        return result ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 给用户绑定角色
     */
    @PostMapping("/{userId}/roles")
    public Result<Void> bindRoles(@PathVariable Long userId, @RequestBody Long[] roleIds) {
        boolean result = userService.bindUserRoles(userId, roleIds);
        return result ? Result.success() : Result.error("绑定角色失败");
    }

    /**
     * 解除用户角色
     */
    @PostMapping("/{userId}/roles/unbind")
    public Result<Void> unbindRoles(@PathVariable Long userId, @RequestBody Long[] roleIds) {
        boolean result = userService.unbindUserRoles(userId, roleIds);
        return result ? Result.success() : Result.error("解除角色失败");
    }

    /**
     * 给用户绑定组织
     */
    @PostMapping("/{userId}/org")
    public Result<Void> bindOrg(@PathVariable Long userId, @RequestParam Long orgId, @RequestParam(required = false, defaultValue = "0") Integer isMain) {
        boolean result = userService.bindUserOrg(userId, orgId, isMain);
        return result ? Result.success() : Result.error("绑定组织失败");
    }

    /**
     * 解除用户组织
     */
    @PostMapping("/{userId}/org/unbind")
    public Result<Void> unbindOrg(@PathVariable Long userId, @RequestParam Long orgId) {
        boolean result = userService.unbindUserOrg(userId, orgId);
        return result ? Result.success() : Result.error("解除组织失败");
    }

    /**
     * 查询用户的角色ID列表
     */
    @GetMapping("/{userId}/roles")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        List<Long> roleIds = userService.selectUserRoleIds(userId);
        return Result.success(roleIds);
    }

    /**
     * 查询用户的组织ID列表
     */
    @GetMapping("/{userId}/orgs")
    public Result<List<Long>> getUserOrgIds(@PathVariable Long userId) {
        List<Long> orgIds = userService.selectUserOrgIds(userId);
        return Result.success(orgIds);
    }

    /**
     * 批量绑定用户组织
     */
    @PostMapping("/{userId}/orgs")
    public Result<Void> bindOrgs(@PathVariable Long userId, @RequestBody UserOrgBindDTO dto) {
        boolean result = userService.bindUserOrgs(userId, dto.getOrgIds(), dto.getMainOrgId());
        return result ? Result.success() : Result.error("绑定组织失败");
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/resetPwd")
    public Result<Void> resetPwd(@RequestParam Long id, @RequestParam String password) {
        boolean result = userService.resetPassword(id, password);
        return result ? Result.success() : Result.error("重置密码失败");
    }

    /**
     * 更新用户状态
     */
    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        boolean result = userService.updateUserStatus(id, status);
        return result ? Result.success() : Result.error("操作失败");
    }

    /**
     * 更新用户资料
     */
    @PostMapping("/updateProfile")
    public Result<Void> updateProfile(@RequestBody SysUserDTO dto) {
        boolean result = userService.updateUserProfile(dto);
        return result ? Result.success() : Result.error("更新资料失败");
    }
}
