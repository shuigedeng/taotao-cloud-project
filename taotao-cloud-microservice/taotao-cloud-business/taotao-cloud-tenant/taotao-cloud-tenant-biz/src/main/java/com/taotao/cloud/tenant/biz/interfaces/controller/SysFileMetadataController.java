package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.plugin.system.entity.SysFileMetadata;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileGroupMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileMetadataService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文件元数据管理
 */
@RestController
@RequestMapping("/system/file/metadata")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysFileMetadataController {
    
    private final ISysFileMetadataService fileMetadataService;
    private final SysFileGroupMapper fileGroupMapper;
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page<SysFileMetadata>> page(PageQuery query, SysFileMetadata condition) {
        return Result.success(fileMetadataService.page(query, condition));
    }
    
    /**
     * 详情
     */
    @GetMapping("/{id}")
    public Result<SysFileMetadata> detail(@PathVariable Long id) {
        return Result.success(fileMetadataService.getById(id));
    }
    
    /**
     * 根据业务类型和业务ID查询
     */
    @GetMapping("/business/{businessType}/{businessId}")
    public Result<List<SysFileMetadata>> listByBusiness(
            @PathVariable String businessType,
            @PathVariable String businessId) {
        return Result.success(fileMetadataService.listByBusiness(businessType, businessId));
    }
    
    /**
     * 获取文件统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        return Result.success(fileGroupMapper.selectFileStatistics());
    }
    
    /**
     * 批量删除
     */
    @DeleteMapping("/{fileIds}")
    public Result<Void> remove(@PathVariable String[] fileIds) {
        fileMetadataService.removeBatch(fileIds);
        return Result.success();
    }
    
    /**
     * 更新文件元数据（如移动分组）
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody SysFileMetadata metadata) {
        return Result.success(fileMetadataService.updateById(metadata));
    }
}
