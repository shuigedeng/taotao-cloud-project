package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileMetadata;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileStorageConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileMetadataMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileMetadataService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.file.core.FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文件元数据Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileMetadataServiceImpl extends ServiceImpl<SysFileMetadataMapper, SysFileMetadata>
        implements ISysFileMetadataService {
    
    private final FileManager fileManager;
    
    @Override
    public Page<SysFileMetadata> page(PageQuery query, SysFileMetadata condition) {
        LambdaQueryWrapper<SysFileMetadata> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(condition.getOriginalName())) {
            wrapper.like(SysFileMetadata::getOriginalName, condition.getOriginalName());
        }
        
        if (StrUtil.isNotBlank(condition.getStorageType())) {
            wrapper.eq(SysFileMetadata::getStorageType, condition.getStorageType());
        }
        
        if (StrUtil.isNotBlank(condition.getBusinessType())) {
            wrapper.eq(SysFileMetadata::getBusinessType, condition.getBusinessType());
        }
        
        if (StrUtil.isNotBlank(condition.getBusinessId())) {
            wrapper.eq(SysFileMetadata::getBusinessId, condition.getBusinessId());
        }
        
        if (condition.getUploaderId() != null) {
            wrapper.eq(SysFileMetadata::getUploaderId, condition.getUploaderId());
        }
        
        if (condition.getGroupId() != null) {
            wrapper.eq(SysFileMetadata::getGroupId, condition.getGroupId());
        }
        
        if (StrUtil.isNotBlank(condition.getMimeType())) {
            wrapper.likeRight(SysFileMetadata::getMimeType, condition.getMimeType());
        }
        
        wrapper.eq(SysFileMetadata::getStatus, "1");
        
        wrapper.orderByDesc(SysFileMetadata::getUploadTime);
        
        Page<SysFileMetadata> page = new Page<>(query.getPageNum(), query.getPageSize());
        return this.baseMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<SysFileMetadata> listByBusiness(String businessType, String businessId) {
        return this.lambdaQuery()
                .eq(SysFileMetadata::getBusinessType, businessType)
                .eq(SysFileMetadata::getBusinessId, businessId)
                .eq(SysFileMetadata::getStatus, 1)
                .orderByDesc(SysFileMetadata::getUploadTime)
                .list();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(String[] fileIds) {
        for (String fileId : fileIds) {
            try {
                SysFileMetadata fileMetadata = this.getById(fileId);
                fileManager.delete(fileMetadata.getFileId());
            } catch (Exception e) {
                log.error("删除文件失败: {}", fileId, e);
            }
        }
    }
}
