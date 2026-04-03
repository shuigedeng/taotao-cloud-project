package com.taotao.cloud.tenant.biz.application.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelColumnConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysExcelColumnConfigMapper;
import com.mdframe.forge.starter.excel.model.ExcelColumnConfig;
import com.mdframe.forge.starter.excel.spi.ExcelConfigProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel列配置提供者实现
 * 从数据库读取列配置
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemExcelConfigProvider implements ExcelConfigProvider {

    private final SysExcelColumnConfigMapper columnConfigMapper;

    @Override
    public List<ExcelColumnConfig> getColumnConfigs(String configKey) {
        LambdaQueryWrapper<SysExcelColumnConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExcelColumnConfig::getConfigKey, configKey)
               .orderByAsc(SysExcelColumnConfig::getOrderNum);

        List<SysExcelColumnConfig> configList = columnConfigMapper.selectList(wrapper);

        if (configList == null || configList.isEmpty()) {
            log.warn("未找到列配置: {}", configKey);
            return null;
        }

        // 转换为 ExcelColumnConfig
        return configList.stream().map(config -> {
            ExcelColumnConfig columnConfig = new ExcelColumnConfig();
            BeanUtils.copyProperties(config, columnConfig);
            return columnConfig;
        }).collect(Collectors.toList());
    }
}
