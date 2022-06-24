package com.taotao.cloud.logger.mztlog.web.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.logger.mztlog.web.repository.LogRecordRepository;
import com.taotao.cloud.logger.mztlog.web.repository.mapper.LogRecordMapper;
import com.taotao.cloud.logger.mztlog.web.repository.po.LogRecordPO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogRecordRepositoryImpl extends ServiceImpl<LogRecordMapper, LogRecordPO> implements LogRecordRepository {

    @Override
    public List<LogRecordPO> queryLog(String bizNo, String type) {
        LogRecordMapper baseMapper = super.getBaseMapper();
        QueryWrapper<LogRecordPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(LogRecordPO::getType, type)
                .eq(LogRecordPO::getBizNo, bizNo)
                .orderByDesc(LogRecordPO::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LogRecordPO> queryLog(String bizNo, String type, String subType) {
        LogRecordMapper baseMapper = super.getBaseMapper();
        QueryWrapper<LogRecordPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(LogRecordPO::getType, type)
                .eq(LogRecordPO::getSubType, subType)
                .eq(LogRecordPO::getBizNo, bizNo)
                .orderByDesc(LogRecordPO::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LogRecordPO> queryLog(String type) {
        QueryWrapper<LogRecordPO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(LogRecordPO::getType, type);
        return baseMapper.selectList(wrapper);
    }

}
