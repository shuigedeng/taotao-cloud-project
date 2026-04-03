package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleQuery;
import com.mdframe.forge.plugin.system.entity.SysRole;
import com.taotao.cloud.tenant.biz.application.service.service.ISysRoleService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理Controller
 */
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysRoleController {

    private final ISysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    public Result<IPage<SysRole>> page(SysRoleQuery query) {
        IPage<SysRole> page = roleService.selectRolePage(query);
        return Result.success(page);
    }

    /**
     * 根据ID查询角色详情
     */
    @PostMapping("/getById")
    public Result<SysRole> getById(@RequestParam Long id) {
        SysRole role = roleService.selectRoleById(id);
        return Result.success(role);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysRoleDTO dto) {
        boolean result = roleService.insertRole(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改角色
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysRoleDTO dto) {
        boolean result = roleService.updateRole(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除角色
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = roleService.deleteRoleById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除角色
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] ids) {
        boolean result = roleService.deleteRoleByIds(ids);
        return result ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 给角色绑定资源（菜单/按钮/接口）
     */
    @PostMapping("/{roleId}/resources")
    public Result<Void> bindResources(@PathVariable Long roleId, @RequestBody Long[] resourceIds) {
        boolean result = roleService.bindRoleResources(roleId, resourceIds);
        return result ? Result.success() : Result.error("绑定资源失败");
    }

    /**
     * 解除角色资源
     */
    @PostMapping("/{roleId}/resources/unbind")
    public Result<Void> unbindResources(@PathVariable Long roleId, @RequestBody Long[] resourceIds) {
        boolean result = roleService.unbindRoleResources(roleId, resourceIds);
        return result ? Result.success() : Result.error("解除资源失败");
    }

    /**
     * 查询角色的资源ID列表
     */
    @GetMapping("/{roleId}/resources")
    public Result<List<Long>> getRoleResourceIds(@PathVariable Long roleId) {
        List<Long> resourceIds = roleService.selectRoleResourceIds(roleId);
        return Result.success(resourceIds);
    }
}
