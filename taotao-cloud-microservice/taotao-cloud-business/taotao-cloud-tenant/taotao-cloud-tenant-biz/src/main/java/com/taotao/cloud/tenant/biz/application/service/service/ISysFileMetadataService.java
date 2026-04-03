package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileMetadata;
import com.mdframe.forge.starter.core.domain.PageQuery;

import java.util.List;

/**
 * 文件元数据Service
 */
public interface ISysFileMetadataService extends IService<SysFileMetadata> {
    
    /**
     * 分页查询
     */
    Page<SysFileMetadata> page(PageQuery query, SysFileMetadata condition);
    
    /**
     * 根据业务类型和业务ID查询
     */
    List<SysFileMetadata> listByBusiness(String businessType, String businessId);
    
    /**
     * 批量删除
     */
    void removeBatch(String[] fileIds);
}
