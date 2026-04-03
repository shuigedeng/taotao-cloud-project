package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceQuery;
import com.mdframe.forge.plugin.system.entity.SysResource;
import com.taotao.cloud.tenant.biz.application.service.service.ISysResourceService;
import com.mdframe.forge.plugin.system.vo.UserResourceTreeVO;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import com.mdframe.forge.starter.core.annotation.log.OperationLog;
import com.mdframe.forge.starter.core.domain.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源管理Controller
 */
@RestController
@RequestMapping("/system/resource")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysResourceController {

    private final ISysResourceService resourceService;

    /**
     * 分页查询资源列表
     */
    @GetMapping("/page")
    public Result<IPage<SysResource>> page(SysResourceQuery query) {
        IPage<SysResource> page = resourceService.selectResourcePage(query);
        return Result.success(page);
    }

    /**
     * 查询资源树形列表
     */
    @GetMapping("/tree")
    public Result<List<SysResource>> tree(SysResourceQuery query) {
        List<SysResource> list = resourceService.selectResourceTree(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询资源详情
     */
    @PostMapping("/getById")
    public Result<SysResource> getById(@RequestParam Long id) {
        SysResource resource = resourceService.selectResourceById(id);
        return Result.success(resource);
    }

    /**
     * 新增资源
     */
    @PostMapping("/add")
    @OperationLog(module = "资源管理", type = OperationType.ADD, desc = "新增资源")
    public Result<Void> add(@RequestBody SysResourceDTO dto) {
        boolean result = resourceService.insertResource(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改资源
     */
    @PostMapping("/edit")
    @OperationLog(module = "资源管理", type = OperationType.UPDATE, desc = "修改资源")
    public Result<Void> edit(@RequestBody SysResourceDTO dto) {
        boolean result = resourceService.updateResource(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除资源
     */
    @PostMapping("/remove")
    @OperationLog(module = "资源管理", type = OperationType.DELETE, desc = "删除资源")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = resourceService.deleteResourceById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 查询当前用户的资源树（包含菜单和按钮权限）
     */
    @GetMapping("/current/tree")
    public Result<List<UserResourceTreeVO>> getCurrentUserResourceTree() {
        List<UserResourceTreeVO> tree = resourceService.selectCurrentUserResourceTree();
        return Result.success(tree);
    }

    /**
     * 查询当前用户的菜单树（仅包含目录和菜单，不包含按钮）
     */
    @GetMapping("/current/menu")
    public Result<List<UserResourceTreeVO>> getCurrentUserMenuTree() {
        List<UserResourceTreeVO> menuTree = resourceService.selectCurrentUserMenuTree();
        return Result.success(menuTree);
    }

    /**
     * 查询当前用户的权限标识列表（按钮权限）
     */
    @GetMapping("/current/permissions")
    public Result<List<String>> getCurrentUserPermissions() {
        List<String> permissions = resourceService.selectCurrentUserPermissions();
        return Result.success(permissions);
    }
}
