package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysDictTypeDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysDictTypeQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysDictType;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysDictTypeMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDictTypeService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 字典类型Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final SysDictTypeMapper dictTypeMapper;

    @Override
    public Page<SysDictType> selectDictTypePage(PageQuery pageQuery, SysDictTypeQuery query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getTenantId() != null, SysDictType::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getDictName()), SysDictType::getDictName, query.getDictName())
                .eq(StringUtils.isNotBlank(query.getDictType()), SysDictType::getDictType, query.getDictType())
                .eq(query.getDictStatus() != null, SysDictType::getDictStatus, query.getDictStatus())
                .orderByDesc(SysDictType::getCreateTime);
        return dictTypeMapper.selectPage(pageQuery.toPage(), wrapper);
    }

    @Override
    public List<SysDictType> selectDictTypeList(SysDictTypeQuery query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        // 添加空值检查,防止NPE
        if (query != null) {
            wrapper.eq(query.getTenantId() != null, SysDictType::getTenantId, query.getTenantId())
                    .like(StringUtils.isNotBlank(query.getDictName()), SysDictType::getDictName, query.getDictName())
                    .eq(StringUtils.isNotBlank(query.getDictType()), SysDictType::getDictType, query.getDictType())
                    .eq(query.getDictStatus() != null, SysDictType::getDictStatus, query.getDictStatus());
        }
        wrapper.orderByDesc(SysDictType::getCreateTime);
        return dictTypeMapper.selectList(wrapper);
    }

    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    @Override
    public boolean insertDictType(SysDictTypeDTO dto) {
        SysDictType dictType = new SysDictType();
        BeanUtil.copyProperties(dto, dictType);
        return dictTypeMapper.insert(dictType) > 0;
    }

    @Override
    public boolean updateDictType(SysDictTypeDTO dto) {
        SysDictType dictType = new SysDictType();
        BeanUtil.copyProperties(dto, dictType);
        return dictTypeMapper.updateById(dictType) > 0;
    }

    @Override
    public boolean deleteDictTypeById(Long dictId) {
        return dictTypeMapper.deleteById(dictId) > 0;
    }

    @Override
    public boolean deleteDictTypeByIds(Long[] dictIds) {
        return dictTypeMapper.deleteBatchIds(Arrays.asList(dictIds)) > 0;
    }
}
