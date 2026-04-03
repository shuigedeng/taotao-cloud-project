package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelColumnConfig;

import java.util.List;

/**
 * Excel列配置Service
 */
public interface ISysExcelColumnConfigService extends IService<SysExcelColumnConfig> {
    
    /**
     * 根据配置键查询列配置
     */
    List<SysExcelColumnConfig> listByConfigKey(String configKey);
    
    /**
     * 批量保存列配置
     */
    void saveBatch(String configKey, List<SysExcelColumnConfig> columns);
    
    /**
     * 删除配置的所有列
     */
    void deleteByConfigKey(String configKey);
}
