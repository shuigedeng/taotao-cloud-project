package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileMetadata;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileMetadataMapper;
import com.mdframe.forge.starter.file.model.FileMetadata;
import com.mdframe.forge.starter.file.spi.FileMetadataPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 文件元数据持久化实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemFileMetadataPersistence implements FileMetadataPersistence {
    
    private final SysFileMetadataMapper metadataMapper;
    
    @Override
    public void save(FileMetadata metadata) {
        SysFileMetadata entity = new SysFileMetadata();
        BeanUtil.copyProperties(metadata, entity);
        entity.setStatus(1);
        metadataMapper.insert(entity);
    }
    
    @Override
    public FileMetadata getById(String fileId) {
        SysFileMetadata entity = metadataMapper.selectOne(
            new LambdaQueryWrapper<SysFileMetadata>()
                .eq(SysFileMetadata::getFileId, fileId)
                .eq(SysFileMetadata::getStatus, 1)
        );
        
        if (entity == null) {
            return null;
        }
        
        return convertToFileMetadata(entity);
    }
    
    @Override
    public FileMetadata getByMd5(String md5) {
        if (md5 == null || md5.isEmpty()) {
            return null;
        }
        
        SysFileMetadata entity = metadataMapper.selectOne(
            new LambdaQueryWrapper<SysFileMetadata>()
                .eq(SysFileMetadata::getMd5, md5)
                .eq(SysFileMetadata::getStatus, 1)
                .last("LIMIT 1")
        );
        
        if (entity == null) {
            return null;
        }
        
        return convertToFileMetadata(entity);
    }
    
    @Override
    public void incrementDownloadCount(String fileId) {
        metadataMapper.incrementDownloadCount(fileId);
    }
    
    @Override
    public void delete(String fileId) {
        // 逻辑删除
        metadataMapper.update(null,
            new LambdaUpdateWrapper<SysFileMetadata>()
                .eq(SysFileMetadata::getFileId, fileId)
                .set(SysFileMetadata::getStatus, 0)
        );
    }
    
    @Override
    public boolean checkPermission(String fileId, Long userId) {
        SysFileMetadata entity = metadataMapper.selectOne(
            new LambdaQueryWrapper<SysFileMetadata>()
                .eq(SysFileMetadata::getFileId, fileId)
                .eq(SysFileMetadata::getStatus, 1)
        );
        
        if (entity == null) {
            return false;
        }
        
        // 公开文件，所有人可访问
        if (!Boolean.TRUE.equals(entity.getIsPrivate())) {
            return true;
        }
        
        // 私有文件，检查是否是上传者
        if (userId == null) {
            try {
                userId = StpUtil.getLoginIdAsLong();
            } catch (Exception e) {
                return false;
            }
        }
        
        return entity.getUploaderId() != null && entity.getUploaderId().equals(userId);
    }
    
    /**
     * 转换为FileMetadata
     */
    private FileMetadata convertToFileMetadata(SysFileMetadata entity) {
        return FileMetadata.builder()
                .fileId(entity.getFileId())
                .originalName(entity.getOriginalName())
                .storageName(entity.getStorageName())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .mimeType(entity.getMimeType())
                .extension(entity.getExtension())
                .md5(entity.getMd5())
                .storageType(entity.getStorageType())
                .bucket(entity.getBucket())
                .accessUrl(entity.getAccessUrl())
                .thumbnailUrl(entity.getThumbnailUrl())
                .businessType(entity.getBusinessType())
                .businessId(entity.getBusinessId())
                .uploaderId(entity.getUploaderId())
                .uploadTime(entity.getUploadTime())
                .expireTime(entity.getExpireTime())
                .isPrivate(entity.getIsPrivate())
                .downloadCount(entity.getDownloadCount())
                .build();
    }
}
