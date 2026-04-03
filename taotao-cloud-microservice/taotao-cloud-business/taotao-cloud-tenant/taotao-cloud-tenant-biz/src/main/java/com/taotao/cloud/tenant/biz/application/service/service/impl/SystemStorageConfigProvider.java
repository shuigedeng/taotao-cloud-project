package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileStorageConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysFileStorageConfigMapper;
import com.mdframe.forge.starter.file.model.StorageConfig;
import com.mdframe.forge.starter.file.spi.StorageConfigProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 存储配置提供者实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemStorageConfigProvider implements StorageConfigProvider {
    
    private final SysFileStorageConfigMapper configMapper;
    
    @Override
    //@Cacheable(value = "storage_config", key = "'default'")
    public StorageConfig getDefaultConfig() {
        SysFileStorageConfig config = configMapper.selectOne(
            new LambdaQueryWrapper<SysFileStorageConfig>()
                .eq(SysFileStorageConfig::getIsDefault, true)
                .eq(SysFileStorageConfig::getEnabled, true)
                .last("LIMIT 1")
        );
        
        if (config == null) {
            log.warn("未找到默认存储配置");
            return null;
        }
        
        return convertToStorageConfig(config);
    }
    
    @Override
    //@Cacheable(value = "storage_config", key = "#storageType")
    public StorageConfig getConfigByType(String storageType) {
        SysFileStorageConfig config = configMapper.selectOne(
            new LambdaQueryWrapper<SysFileStorageConfig>()
                .eq(SysFileStorageConfig::getStorageType, storageType)
                .eq(SysFileStorageConfig::getEnabled, true)
                .last("LIMIT 1")
        );
        
        if (config == null) {
            log.warn("未找到存储配置: {}", storageType);
            return null;
        }
        
        return convertToStorageConfig(config);
    }
    
    @Override
    //@Cacheable(value = "storage_config", key = "'all'")
    public List<StorageConfig> getAllEnabledConfigs() {
        List<SysFileStorageConfig> configs = configMapper.selectList(
            new LambdaQueryWrapper<SysFileStorageConfig>()
                .eq(SysFileStorageConfig::getEnabled, true)
                .orderByAsc(SysFileStorageConfig::getOrderNum)
        );
        
        return configs.stream()
                .map(this::convertToStorageConfig)
                .collect(Collectors.toList());
    }
    
    @Override
    //@CacheEvict(value = "storage_config", allEntries = true)
    public void refreshConfig() {
        log.info("刷新存储配置缓存");
    }
    
    /**
     * 转换为StorageConfig
     */
    private StorageConfig convertToStorageConfig(SysFileStorageConfig entity) {
        StorageConfig config = new StorageConfig();
        BeanUtil.copyProperties(entity, config);
        config.setId(entity.getId());
        return config;
    }
}
