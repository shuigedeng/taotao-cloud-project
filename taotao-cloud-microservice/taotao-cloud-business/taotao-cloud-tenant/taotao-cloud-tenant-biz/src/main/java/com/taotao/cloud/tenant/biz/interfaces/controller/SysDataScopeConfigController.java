package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import com.mdframe.forge.starter.datascope.entity.SysDataScopeConfig;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDataScopeConfigService;
import com.mdframe.forge.starter.core.annotation.log.OperationLog;
import com.mdframe.forge.starter.core.domain.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据权限配置Controller
 */
@RestController
@RequestMapping("/system/dataScopeConfig")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysDataScopeConfigController {

    private final ISysDataScopeConfigService dataScopeConfigService;

    /**
     * 分页查询数据权限配置列表
     */
    @GetMapping("/page")
    @OperationLog(module = "数据权限配置管理", type = OperationType.QUERY, desc = "分页查询配置列表")
    public Result<Page<SysDataScopeConfig>> page(PageQuery pageQuery, SysDataScopeConfig query) {
        Page<SysDataScopeConfig> page = dataScopeConfigService.selectConfigPage(pageQuery, query);
        return Result.success(page);
    }

    /**
     * 查询数据权限配置列表
     */
    @GetMapping("/list")
    @OperationLog(module = "数据权限配置管理", type = OperationType.QUERY, desc = "查询配置列表")
    public Result<List<SysDataScopeConfig>> list(SysDataScopeConfig query) {
        List<SysDataScopeConfig> list = dataScopeConfigService.selectConfigList(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询配置详情
     */
    @PostMapping("/getById")
    public Result<SysDataScopeConfig> getById(@RequestParam Long id) {
        SysDataScopeConfig config = dataScopeConfigService.selectConfigById(id);
        return Result.success(config);
    }

    /**
     * 新增数据权限配置
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysDataScopeConfig config) {
        boolean result = dataScopeConfigService.insertConfig(config);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改数据权限配置
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysDataScopeConfig config) {
        boolean result = dataScopeConfigService.updateConfig(config);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除数据权限配置
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = dataScopeConfigService.deleteConfigById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除数据权限配置
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] ids) {
        boolean result = dataScopeConfigService.deleteConfigByIds(ids);
        return result ? Result.success() : Result.error("批量删除失败");
    }
}
