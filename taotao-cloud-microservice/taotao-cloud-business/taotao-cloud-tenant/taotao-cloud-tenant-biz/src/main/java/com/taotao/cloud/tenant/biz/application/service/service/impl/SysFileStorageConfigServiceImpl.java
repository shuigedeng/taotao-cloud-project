package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileStorageConfig;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysPost;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileStorageConfigMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysFileStorageConfigService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.file.model.StorageConfig;
import com.mdframe.forge.starter.file.spi.StorageConfigProvider;
import com.mdframe.forge.starter.file.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 文件存储配置Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileStorageConfigServiceImpl extends ServiceImpl<SysFileStorageConfigMapper, SysFileStorageConfig>
        implements ISysFileStorageConfigService {
    
    private final StorageConfigProvider configProvider;
    private final Map<String, FileStorage> storageMap;
    
    @Override
    public Page<SysFileStorageConfig> page(PageQuery query, SysFileStorageConfig condition) {
        LambdaQueryWrapper<SysFileStorageConfig> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(condition.getConfigName())) {
            wrapper.like(SysFileStorageConfig::getConfigName, condition.getConfigName());
        }
        
        if (StrUtil.isNotBlank(condition.getStorageType())) {
            wrapper.eq(SysFileStorageConfig::getStorageType, condition.getStorageType());
        }
        
        if (condition.getEnabled() != null) {
            wrapper.eq(SysFileStorageConfig::getEnabled, condition.getEnabled());
        }
        wrapper.orderByDesc(SysFileStorageConfig::getIsDefault)
                .orderByAsc(SysFileStorageConfig::getOrderNum);
        Page<SysFileStorageConfig> page = new Page<>(query.getPageNum(), query.getPageSize());
        return this.baseMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        // 取消所有默认配置
        this.lambdaUpdate()
                .set(SysFileStorageConfig::getIsDefault, false)
                .update();
        
        // 设置新的默认配置
        this.lambdaUpdate()
                .eq(SysFileStorageConfig::getId, id)
                .set(SysFileStorageConfig::getIsDefault, true)
                .update();
        
        // 刷新配置缓存
        configProvider.refreshConfig();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Boolean enabled) {
        this.lambdaUpdate()
                .eq(SysFileStorageConfig::getId, id)
                .set(SysFileStorageConfig::getEnabled, enabled)
                .update();
        
        // 刷新配置缓存
        configProvider.refreshConfig();
    }
    
    @Override
    public boolean testConnection(Long id) {
        SysFileStorageConfig config = this.getById(id);
        if (config == null) {
            return false;
        }
        
        try {
            StorageConfig storage = configProvider.getConfigByType(config.getStorageType());
            if (storage == null) {
                log.warn("未找到存储实现: {}", config.getStorageType());
                return false;
            }
            
            // 这里可以添加具体的连接测试逻辑
            // 例如：尝试列举bucket、上传小文件等
            return true;
        } catch (Exception e) {
            log.error("测试连接失败", e);
            return false;
        }
    }
}
