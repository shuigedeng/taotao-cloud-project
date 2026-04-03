package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDataScopeConfigService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.datascope.mapper.SysDataScopeConfigMapper;
import com.mdframe.forge.starter.datascope.service.IDataScopeService;
import com.mdframe.forge.starter.datascope.entity.SysDataScopeConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 数据权限配置服务实现
 */
@Service
@RequiredArgsConstructor
public class SysDataScopeConfigServiceImpl implements ISysDataScopeConfigService {

    private final SysDataScopeConfigMapper dataScopeConfigMapper;
    private final IDataScopeService dataScopeService;

    @Override
    public Page<SysDataScopeConfig> selectConfigPage(PageQuery pageQuery, SysDataScopeConfig query) {
        LambdaQueryWrapper<SysDataScopeConfig> wrapper = buildQueryWrapper(query);
        return dataScopeConfigMapper.selectPage(pageQuery.toPage(), wrapper);
    }

    @Override
    public List<SysDataScopeConfig> selectConfigList(SysDataScopeConfig query) {
        LambdaQueryWrapper<SysDataScopeConfig> wrapper = buildQueryWrapper(query);
        return dataScopeConfigMapper.selectList(wrapper);
    }

    @Override
    public SysDataScopeConfig selectConfigById(Long id) {
        return dataScopeConfigMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertConfig(SysDataScopeConfig config) {
        int result = dataScopeConfigMapper.insert(config);
        if (result > 0) {
            // 刷新数据权限配置缓存
            dataScopeService.refreshDataScopeCache();
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SysDataScopeConfig config) {
        int result = dataScopeConfigMapper.updateById(config);
        if (result > 0) {
            // 刷新数据权限配置缓存
            dataScopeService.refreshDataScopeCache();
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigById(Long id) {
        int result = dataScopeConfigMapper.deleteById(id);
        if (result > 0) {
            // 刷新数据权限配置缓存
            dataScopeService.refreshDataScopeCache();
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigByIds(Long[] ids) {
        int result = dataScopeConfigMapper.deleteBatchIds(Arrays.asList(ids));
        if (result > 0) {
            // 刷新数据权限配置缓存
            dataScopeService.refreshDataScopeCache();
        }
        return result > 0;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysDataScopeConfig> buildQueryWrapper(SysDataScopeConfig query) {
        LambdaQueryWrapper<SysDataScopeConfig> wrapper = new LambdaQueryWrapper<>();
        
        if (query != null) {
            // 资源编码
            wrapper.eq(StringUtils.isNotBlank(query.getResourceCode()), 
                SysDataScopeConfig::getResourceCode, query.getResourceCode());
            // 资源名称
            wrapper.like(StringUtils.isNotBlank(query.getResourceName()), 
                SysDataScopeConfig::getResourceName, query.getResourceName());
            // Mapper方法
            wrapper.like(StringUtils.isNotBlank(query.getMapperMethod()), 
                SysDataScopeConfig::getMapperMethod, query.getMapperMethod());
            // 是否启用
            wrapper.eq(query.getEnabled() != null, 
                SysDataScopeConfig::getEnabled, query.getEnabled());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(SysDataScopeConfig::getCreateTime);
        
        return wrapper;
    }
}
