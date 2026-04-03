package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.plugin.system.entity.SysFileStorageConfig;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileStorageConfigService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文件存储配置管理
 */
@RestController
@RequestMapping("/system/storage/config")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysFileStorageConfigController {
    
    private final ISysFileStorageConfigService storageConfigService;
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<SysFileStorageConfig>> page(PageQuery query, SysFileStorageConfig condition) {
        return Result.success(storageConfigService.page(query, condition));
    }
    
    /**
     * 详情
     */
    @PostMapping("/detail")
    public Result<SysFileStorageConfig> detail(@RequestParam Long id) {
        return Result.success(storageConfigService.getById(id));
    }
    
    /**
     * 新增
     */
    @PostMapping
    public Result<Void> add(@RequestBody SysFileStorageConfig config) {
        storageConfigService.save(config);
        return Result.success();
    }
    
    /**
     * 修改
     */
    @PutMapping
    public Result<Void> edit(@RequestBody SysFileStorageConfig config) {
        storageConfigService.updateById(config);
        return Result.success();
    }
    
    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            storageConfigService.removeById(id);
        }
        return Result.success();
    }
    
    /**
     * 设置默认配置
     */
    @PutMapping("/default/{id}")
    public Result<Void> setDefault(@PathVariable Long id) {
        storageConfigService.setDefault(id);
        return Result.success();
    }
    
    /**
     * 启用/禁用
     */
    @PutMapping("/enabled/{id}/{enabled}")
    public Result<Void> updateEnabled(@PathVariable Long id, @PathVariable Boolean enabled) {
        storageConfigService.updateEnabled(id, enabled);
        return Result.success();
    }
    
    /**
     * 测试连接
     */
    @PostMapping("/test/{id}")
    public Result<Boolean> testConnection(@PathVariable Long id) {
        return Result.success(storageConfigService.testConnection(id));
    }
}
