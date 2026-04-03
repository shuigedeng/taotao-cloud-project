package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileStorageConfig;
import com.mdframe.forge.starter.core.domain.PageQuery;

/**
 * 文件存储配置Service
 */
public interface ISysFileStorageConfigService extends IService<SysFileStorageConfig> {
    
    /**
     * 分页查询
     */
    Page<SysFileStorageConfig> page(PageQuery query, SysFileStorageConfig condition);
    
    /**
     * 设置默认配置
     */
    void setDefault(Long id);
    
    /**
     * 启用/禁用配置
     */
    void updateEnabled(Long id, Boolean enabled);
    
    /**
     * 测试连接
     */
    boolean testConnection(Long id);
}
