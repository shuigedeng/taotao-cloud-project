package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskCirculateMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskCirculateService;
import org.springframework.stereotype.Service;

/**
 * 流程传阅
 */
@Service
public class FlowTaskCirculateServiceImpl extends ServiceImpl<FlowTaskCirculateMapper, FlowTaskCirculateEntity> implements FlowTaskCirculateService {

    @Override
    public void deleteByTaskId(String taskId) {
        QueryWrapper<FlowTaskCirculateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskCirculateEntity::getTaskId, taskId);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteByNodeId(String nodeId) {
        QueryWrapper<FlowTaskCirculateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskCirculateEntity::getTaskNodeId, nodeId);
        this.remove(queryWrapper);
    }

    @Override
    public void create(List<FlowTaskCirculateEntity> entitys) {
        for (FlowTaskCirculateEntity entity : entitys) {
            this.save(entity);
        }
    }
}
