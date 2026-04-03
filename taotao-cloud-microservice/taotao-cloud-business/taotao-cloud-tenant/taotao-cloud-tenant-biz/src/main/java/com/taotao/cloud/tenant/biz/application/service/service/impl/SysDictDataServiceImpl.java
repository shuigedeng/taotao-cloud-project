package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysDictData;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysDictDataMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDictDataService;
import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 字典数据Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    private final SysDictDataMapper dictDataMapper;

    @Override
    public Page<SysDictData> selectDictDataPage(PageQuery pageQuery, SysDictDataQuery query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getTenantId() != null, SysDictData::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
                .eq(StringUtils.isNotBlank(query.getDictType()), SysDictData::getDictType, query.getDictType())
                .eq(query.getDictStatus() != null, SysDictData::getDictStatus, query.getDictStatus())
                .orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectPage(pageQuery.toPage(), wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataList(SysDictDataQuery query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        // 添加空值检查,防止NPE
        if (query != null) {
            wrapper.eq(query.getTenantId() != null, SysDictData::getTenantId, query.getTenantId())
                    .like(StringUtils.isNotBlank(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
                    .eq(StringUtils.isNotBlank(query.getDictType()), SysDictData::getDictType, query.getDictType())
                    .eq(query.getDictStatus() != null, SysDictData::getDictStatus, query.getDictStatus());
        }
        wrapper.orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectList(wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictStatus, 1)
                .orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectList(wrapper);
    }

    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectById(dictCode);
    }

    @Override
    public boolean insertDictData(SysDictDataDTO dto) {
        SysDictData dictData = new SysDictData();
        BeanUtil.copyProperties(dto, dictData);
        return dictDataMapper.insert(dictData) > 0;
    }

    @Override
    public boolean updateDictData(SysDictDataDTO dto) {
        SysDictData dictData = new SysDictData();
        BeanUtil.copyProperties(dto, dictData);
        return dictDataMapper.updateById(dictData) > 0;
    }

    @Override
    public boolean deleteDictDataById(Long dictCode) {
        return dictDataMapper.deleteById(dictCode) > 0;
    }

    @Override
    public boolean deleteDictDataByIds(Long[] dictCodes) {
        return dictDataMapper.deleteBatchIds(Arrays.asList(dictCodes)) > 0;
    }
}
