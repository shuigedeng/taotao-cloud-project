package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelColumnConfig;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelExportConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysExcelColumnConfigMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysExcelExportConfigMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysExcelExportConfigService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Excel导出配置Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysExcelExportConfigServiceImpl extends ServiceImpl<SysExcelExportConfigMapper, SysExcelExportConfig>
        implements ISysExcelExportConfigService {
    
    private final SysExcelColumnConfigMapper columnConfigMapper;
    
    @Override
    public Page<SysExcelExportConfig> page(PageQuery query, SysExcelExportConfig condition) {
        LambdaQueryWrapper<SysExcelExportConfig> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(condition.getConfigKey())) {
            wrapper.like(SysExcelExportConfig::getConfigKey, condition.getConfigKey());
        }
        
        if (StrUtil.isNotBlank(condition.getExportName())) {
            wrapper.like(SysExcelExportConfig::getExportName, condition.getExportName());
        }
        
        if (condition.getStatus() != null) {
            wrapper.eq(SysExcelExportConfig::getStatus, condition.getStatus());
        }
        
        wrapper.orderByDesc(SysExcelExportConfig::getUpdateTime);
        
        Page<SysExcelExportConfig> page = new Page<>(query.getPageNum(), query.getPageSize());
        return this.baseMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        this.lambdaUpdate()
                .eq(SysExcelExportConfig::getId, id)
                .set(SysExcelExportConfig::getStatus, status)
                .update();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysExcelExportConfig copyConfig(Long id, String newConfigKey) {
        // 查询原配置
        SysExcelExportConfig source = this.getById(id);
        if (source == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 检查新配置键是否已存在
        long count = this.lambdaQuery()
                .eq(SysExcelExportConfig::getConfigKey, newConfigKey)
                .count();
        if (count > 0) {
            throw new RuntimeException("配置键已存在: " + newConfigKey);
        }
        
        // 复制主配置
        SysExcelExportConfig newConfig = new SysExcelExportConfig();
        BeanUtils.copyProperties(source, newConfig);
        newConfig.setId(null);
        newConfig.setConfigKey(newConfigKey);
        newConfig.setExportName(source.getExportName() + "_副本");
        newConfig.setStatus(0); // 默认禁用
        this.save(newConfig);
        
        // 复制列配置
        LambdaQueryWrapper<SysExcelColumnConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExcelColumnConfig::getConfigKey, source.getConfigKey());
        List<SysExcelColumnConfig> columnConfigs = columnConfigMapper.selectList(wrapper);
        
        for (SysExcelColumnConfig columnConfig : columnConfigs) {
            SysExcelColumnConfig newColumn = new SysExcelColumnConfig();
            BeanUtils.copyProperties(columnConfig, newColumn);
            newColumn.setConfigKey(newConfigKey);
            columnConfigMapper.insert(newColumn);
        }
        
        return newConfig;
    }
}
