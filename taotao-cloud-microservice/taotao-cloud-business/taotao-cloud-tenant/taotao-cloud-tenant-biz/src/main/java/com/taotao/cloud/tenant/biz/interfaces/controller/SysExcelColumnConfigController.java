package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.mdframe.forge.plugin.system.entity.SysExcelColumnConfig;
import com.taotao.cloud.tenant.biz.application.service.service.ISysExcelColumnConfigService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Excel列配置管理
 */
@RestController
@RequestMapping("/system/excel/column-config")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysExcelColumnConfigController {
    
    private final ISysExcelColumnConfigService columnConfigService;
    
    /**
     * 根据配置键查询列配置
     */
    @GetMapping("/list")
    public Result<List<SysExcelColumnConfig>> list(@RequestParam String configKey) {
        return Result.success(columnConfigService.listByConfigKey(configKey));
    }
    
    /**
     * 详情
     */
    @PostMapping("/detail")
    public Result<SysExcelColumnConfig> detail(@RequestParam Long id) {
        return Result.success(columnConfigService.getById(id));
    }
    
    /**
     * 新增
     */
    @PostMapping
    public Result<Void> add(@RequestBody SysExcelColumnConfig config) {
        columnConfigService.save(config);
        return Result.success();
    }
    
    /**
     * 修改
     */
    @PutMapping
    public Result<Void> edit(@RequestBody SysExcelColumnConfig config) {
        columnConfigService.updateById(config);
        return Result.success();
    }
    
    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            columnConfigService.removeById(id);
        }
        return Result.success();
    }
    
    /**
     * 批量保存列配置
     */
    @PostMapping("/batch")
    public Result<Void> saveBatch(@RequestParam String configKey, @RequestBody List<SysExcelColumnConfig> columns) {
        columnConfigService.saveBatch(configKey, columns);
        return Result.success();
    }
}
