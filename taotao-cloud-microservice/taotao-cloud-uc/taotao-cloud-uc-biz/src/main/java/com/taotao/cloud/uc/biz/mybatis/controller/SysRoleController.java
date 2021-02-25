// package com.taotao.cloud.uc.biz.controller;
//
//
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.uc.api.dto.RoleDTO;
// import com.taotao.cloud.uc.biz.entity.SysRole;
// import com.taotao.cloud.uc.biz.service.ISysRoleMenuService;
// import com.taotao.cloud.uc.biz.service.ISysRoleService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 角色管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:45
//  */
// @RestController
// @RequestMapping("/role")
// @Api(value = "角色管理API", tags = { "角色管理API" })
// public class SysRoleController {
//
//     @Autowired
//     private ISysRoleService roleService;
//     @Autowired
//     private ISysRoleMenuService roleMenuService;
//
//     @ApiOperation("获取角色列表")
//     @GetMapping
//     @PreAuthorize("hasAuthority('sys:role:view')")
//     public Result<List<SysRole>> getRoleList(@RequestParam String roleName) {
//         return Result.succeed(roleService.selectRoleList(roleName));
//     }
//
//     @ApiOperation("添加角色")
//     @SysOperateLog(description = "添加角色")
//     @PreAuthorize("hasAuthority('sys:role:add')")
//     @PostMapping
//     public Result<Boolean> add(@RequestBody RoleDTO roleDto) {
//         return Result.succeed(roleService.saveRole(roleDto));
//     }
//
//     @ApiOperation("据角色id获取菜单")
//     @SysOperateLog(description = "据角色id获取菜单")
//     @GetMapping("/queryRolePermission/{roleId}")
//     public Result<List<Integer>> getRoleMenus(@PathVariable("roleId") Integer roleId) {
//         return Result.succeed(roleMenuService.getMenuIdByRoleId(roleId));
//     }
//
//     @ApiOperation("更新角色")
//     @SysOperateLog(description = "更新角色")
//     @PreAuthorize("hasAuthority('sys:role:update')")
//     @PutMapping
//     public Result<Boolean> update(@RequestBody RoleDTO roleDto) {
//         return Result.succeed(roleService.updateRole(roleDto));
//     }
//
//     @ApiOperation("更新角色权限")
//     @SysOperateLog(description = "更新角色权限")
//     @PreAuthorize("hasAuthority('sys:role:update')")
//     @PutMapping("/updateRolePermission")
//     public Result<Boolean> updateRolePermission(@RequestBody RoleDTO roleDto) {
//         return Result.succeed(roleService.updateRolePermission(roleDto));
//     }
//
//     @ApiOperation("删除角色以及权限")
//     @SysOperateLog(description = "删除角色以及权限")
//     @PreAuthorize("hasAuthority('sys:role:delete')")
//     @DeleteMapping("/{roleId}")
//     public Result<Boolean> delete(@PathVariable("roleId") Integer roleId) {
// //        return Result.succeed(roleService.removeById(roleId));
//         return null;
//     }
//
//     @ApiOperation("批量删除角色")
//     @SysOperateLog(description = "批量删除角色")
//     @PreAuthorize("hasAuthority('sys:role:delete')")
//     @DeleteMapping("/batchDelete")
//     public Result<Boolean> deleteBatch(@RequestParam(name = "ids") String ids) {
//         List<Integer> listIds = Arrays.stream(ids.split(",")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
//         return Result.succeed(roleService.batchDeleteRoleByIds(listIds));
//     }
// }
//
