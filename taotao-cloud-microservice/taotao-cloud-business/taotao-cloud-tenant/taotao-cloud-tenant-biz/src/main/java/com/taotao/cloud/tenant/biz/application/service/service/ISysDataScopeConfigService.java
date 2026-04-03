package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.datascope.entity.SysDataScopeConfig;
import java.util.List;

/**
 * 数据权限配置服务接口
 */
public interface ISysDataScopeConfigService {

    /**
     * 分页查询数据权限配置列表
     */
    Page<SysDataScopeConfig> selectConfigPage(PageQuery pageQuery, SysDataScopeConfig query);

    /**
     * 查询数据权限配置列表
     */
    List<SysDataScopeConfig> selectConfigList(SysDataScopeConfig query);

    /**
     * 根据ID查询配置详情
     */
    SysDataScopeConfig selectConfigById(Long id);

    /**
     * 新增数据权限配置
     */
    boolean insertConfig(SysDataScopeConfig config);

    /**
     * 修改数据权限配置
     */
    boolean updateConfig(SysDataScopeConfig config);

    /**
     * 删除数据权限配置
     */
    boolean deleteConfigById(Long id);

    /**
     * 批量删除数据权限配置
     */
    boolean deleteConfigByIds(Long[] ids);
}
