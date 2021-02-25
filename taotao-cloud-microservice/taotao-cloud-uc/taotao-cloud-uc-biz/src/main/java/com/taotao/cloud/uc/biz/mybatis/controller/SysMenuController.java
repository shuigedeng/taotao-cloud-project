// package com.taotao.cloud.uc.biz.controller;
//
//
// import com.alibaba.fastjson.JSONArray;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.uc.api.dto.MenuDTO;
// import com.taotao.cloud.uc.api.vo.MenuTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysMenu;
// import com.taotao.cloud.uc.biz.service.ISysMenuService;
// import com.taotao.cloud.uc.biz.utils.UcUtil;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.List;
//
// /**
//  * 菜单管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:37
//  */
// @RestController
// @RequestMapping("/menu")
// @Api(value = "菜单管理API", tags = { "菜单管理API" })
// public class SysMenuController {
//
//     @Autowired
//     private ISysMenuService menuService;
//
//     @ApiOperation("添加菜单")
//     @SysOperateLog(description = "添加菜单")
//     @PreAuthorize("hasAuthority('sys:menu:add')")
//     @PostMapping
//     public Result<Boolean> add(@RequestBody SysMenu menu) {
// //        return Result.succeed(menuService.save(menu));
//         return null;
//     }
//
//     @ApiOperation("获取所有菜单")
//     @GetMapping("/getMenuTree")
//     public Result<List<SysMenu>> getMenuTree() {
//         return Result.succeed(menuService.selectMenuTree(0));
//     }
//
//     @ApiOperation("获取所有菜单")
//     @PreAuthorize("hasAuthority('sys:menu:update')")
//     @GetMapping("/getMenuTreeVos")
//     public Result<List<MenuTreeVo>> getMenuTreeVos() {
//         return Result.succeed(menuService.menuTree());
//     }
//
//     @ApiOperation("修改菜单")
//     @PreAuthorize("hasAuthority('sys:menu:update')")
//     @SysOperateLog(description = "修改菜单")
//     @PostMapping("/update")
//     public Result<Boolean> updateMenu(@RequestBody MenuDTO menuDto) {
//         return Result.succeed(menuService.updateMenuById(menuDto));
//     }
//
//     @ApiOperation("删除菜单")
//     @PreAuthorize("hasAuthority('sys:menu:delete')")
//     @SysOperateLog(description = "删除菜单")
//     @DeleteMapping("/{id}")
//     public Result<Boolean> deleteMenu(@PathVariable("id") Integer id) {
//         return Result.succeed(menuService.removeMenuById(id));
//     }
//
//     @ApiOperation("获取菜单路由")
//     @GetMapping("/getRouters")
//     public Result<JSONArray> getRouters() {
// //        List<SysMenu> list = menuService.list();
//         JSONArray menuJsonArray = new JSONArray();
//         UcUtil.getPermissionJsonArray(menuJsonArray, null, null);
//         return Result.succeed(menuJsonArray);
//     }
// }
//
