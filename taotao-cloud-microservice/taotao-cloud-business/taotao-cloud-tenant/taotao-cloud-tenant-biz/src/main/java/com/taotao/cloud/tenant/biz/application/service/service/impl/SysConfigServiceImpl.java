package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysConfigDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysConfigQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysConfig;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysConfigMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysConfigService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import com.mdframe.forge.starter.trans.annotation.DictTranslate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 系统配置Service实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    private final SysConfigMapper configMapper;

    @Override
    @DictTranslate
    public Page<SysConfig> selectConfigPage(PageQuery pageQuery, SysConfigQuery query) {
        log.info("分页查询参数:{}, 查询条件:{}", pageQuery, query);
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getTenantId() != null, SysConfig::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .eq(StringUtils.isNotBlank(query.getConfigKey()), SysConfig::getConfigKey, query.getConfigKey())
                .eq(StringUtils.isNotBlank(query.getConfigType()), SysConfig::getConfigType, query.getConfigType())
                .orderByAsc(SysConfig::getSort);
        return configMapper.selectPage(pageQuery.toPage(), wrapper);
    }

    @Override
    @DictTranslate
    public List<SysConfig> selectConfigList(SysConfigQuery query) {
        log.info("查询参数:{}",query);
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        // 添加空值检查,防止NPE
        if (query != null) {
            wrapper.eq(query.getTenantId() != null, SysConfig::getTenantId, query.getTenantId())
                    .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                    .eq(StringUtils.isNotBlank(query.getConfigKey()), SysConfig::getConfigKey, query.getConfigKey())
                    .eq(StringUtils.isNotBlank(query.getConfigType()), SysConfig::getConfigType, query.getConfigType());
        }
        wrapper.orderByAsc(SysConfig::getSort);
        return configMapper.selectList(wrapper);
    }

    @Override
    public String selectConfigByKey(String configKey) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configKey).last("LIMIT 1");
        SysConfig config = configMapper.selectOne(wrapper);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public SysConfig selectConfigById(Long configId) {
        return configMapper.selectById(configId);
    }

    @Override
    public boolean insertConfig(SysConfigDTO dto) {
        SysConfig config = new SysConfig();
        BeanUtil.copyProperties(dto, config);
        return configMapper.insert(config) > 0;
    }

    @Override
    public boolean updateConfig(SysConfigDTO dto) {
        SysConfig config = new SysConfig();
        BeanUtil.copyProperties(dto, config);
        return configMapper.updateById(config) > 0;
    }

    @Override
    public boolean deleteConfigById(Long configId) {
        return configMapper.deleteById(configId) > 0;
    }

    @Override
    public boolean deleteConfigByIds(Long[] configIds) {
        return configMapper.deleteBatchIds(Arrays.asList(configIds)) > 0;
    }
}
