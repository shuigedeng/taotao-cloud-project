package com.taotao.cloud.tenant.biz.application.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelColumnConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysExcelColumnConfigMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysExcelColumnConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Excel列配置Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysExcelColumnConfigServiceImpl extends ServiceImpl<SysExcelColumnConfigMapper, SysExcelColumnConfig>
        implements ISysExcelColumnConfigService {
    
    @Override
    public List<SysExcelColumnConfig> listByConfigKey(String configKey) {
        LambdaQueryWrapper<SysExcelColumnConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExcelColumnConfig::getConfigKey, configKey)
                .orderByAsc(SysExcelColumnConfig::getOrderNum);
        return this.list(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(String configKey, List<SysExcelColumnConfig> columns) {
        // 先删除原有配置
        deleteByConfigKey(configKey);
        
        // 批量保存新配置
        if (columns != null && !columns.isEmpty()) {
            for (SysExcelColumnConfig column : columns) {
                column.setConfigKey(configKey);
            }
            this.saveBatch(columns);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByConfigKey(String configKey) {
        LambdaQueryWrapper<SysExcelColumnConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExcelColumnConfig::getConfigKey, configKey);
        this.remove(wrapper);
    }
}
