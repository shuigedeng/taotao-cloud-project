package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.mdframe.forge.plugin.system.entity.SysFileGroup;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileGroupService;
import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;

import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文件分组管理
 */
@RestController
@RequestMapping("/system/file/group")
@RequiredArgsConstructor
@ApiPermissionIgnore
@ApiDecrypt
@ApiEncrypt
public class SysFileGroupController {

    private final ISysFileGroupService fileGroupService;

    /**
     * 获取分组列表（带文件数量）
     */
    @GetMapping("/list")
    public Result<List<SysFileGroup>> list() {
        return Result.success(fileGroupService.listGroupWithFileCount());
    }

    /**
     * 获取文件统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        return Result.success(fileGroupService.getFileStatistics());
    }

    /**
     * 获取分组详情
     */
    @GetMapping("/{id}")
    public Result<SysFileGroup> detail(@PathVariable Long id) {
        return Result.success(fileGroupService.getById(id));
    }

    /**
     * 创建分组
     */
    @PostMapping
    public Result<Boolean> create(@RequestBody SysFileGroup group) {
        return Result.success(fileGroupService.createGroup(group));
    }

    /**
     * 更新分组
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody SysFileGroup group) {
        return Result.success(fileGroupService.updateGroup(group));
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(fileGroupService.deleteGroup(id));
    }
}
