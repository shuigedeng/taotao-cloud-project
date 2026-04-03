package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysExcelExportConfig;
import com.mdframe.forge.starter.core.domain.PageQuery;

/**
 * Excel导出配置Service
 */
public interface ISysExcelExportConfigService extends IService<SysExcelExportConfig> {
    
    /**
     * 分页查询
     */
    Page<SysExcelExportConfig> page(PageQuery query, SysExcelExportConfig condition);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 复制配置
     */
    SysExcelExportConfig copyConfig(Long id, String newConfigKey);
}
