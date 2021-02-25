package com.taotao.cloud.standalone.system.modules.sys.controller;


import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.system.modules.sys.dto.RoleDTO;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    /**
     * 获取角色列表
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('sys:role:view')")
    public R getRoleList(@RequestParam String roleName) {
        return R.ok(roleService.selectRoleList(roleName));
    }

    /**
     * 保存角色以及菜单权限
     *
     * @param roleDto
     * @return
     */
    @SysOperaLog(descrption = "保存角色以及菜单权限")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public R save(@RequestBody RoleDTO roleDto) {
        return R.ok(roleService.saveRoleMenu(roleDto));
    }

    /**
     * 根据角色id获取菜单
     *
     * @param roleId
     * @return
     */
    @SysOperaLog(descrption = "据角色id获取菜单")
    @GetMapping("/findRoleMenus/{roleId}")
    public R findRoleMenus(@PathVariable("roleId") Integer roleId) {
        return R.ok(roleService.findMenuListByRoleId(roleId));
    }


    /**
     * 更新角色以及菜单权限
     * @param roleDto
     * @return
     */
    @SysOperaLog(descrption = "更新角色以及菜单权限")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:update')")
    public R update(@RequestBody RoleDTO roleDto) {
        return R.ok(roleService.updateRoleMenu(roleDto));
    }

    /**
     * 删除角色以及权限
     * @param roleId
     * @return
     */
    @SysOperaLog(descrption = "删除角色以及权限")
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public R delete(@PathVariable("roleId") Integer roleId) {
        return R.ok(roleService.removeById(roleId));
    }



}

