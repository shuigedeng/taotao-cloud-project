package com.taotao.cloud.tenant.biz.application.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelExportConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysExcelExportConfigMapper;
import com.mdframe.forge.starter.excel.model.ExcelExportMetadata;
import com.mdframe.forge.starter.excel.spi.ExcelMetadataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Excel元数据提供者实现
 * 从数据库读取导出配置
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemExcelMetadataProvider implements ExcelMetadataProvider {

    private final SysExcelExportConfigMapper exportConfigMapper;

    @Override
    public ExcelExportMetadata getMetadata(String configKey) {
        LambdaQueryWrapper<SysExcelExportConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysExcelExportConfig::getConfigKey, configKey);
        SysExcelExportConfig config = exportConfigMapper.selectOne(wrapper);

        if (config == null) {
            log.warn("未找到导出配置: {}", configKey);
            return null;
        }

        // 转换为 ExcelExportMetadata
        ExcelExportMetadata metadata = new ExcelExportMetadata();
        BeanUtils.copyProperties(config, metadata);
        return metadata;
    }
}
