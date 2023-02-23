package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineVisibleEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowEngineVisibleMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineVisibleService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程可见
 *
 */
@Service
public class FlowEngineVisibleServiceImpl extends ServiceImpl<FlowEngineVisibleMapper, FlowEngineVisibleEntity> implements FlowEngineVisibleService {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Override
    public List<FlowEngineVisibleEntity> getList(String flowId) {
        QueryWrapper<FlowEngineVisibleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineVisibleEntity::getFlowId, flowId);
        queryWrapper.lambda().orderByAsc(FlowEngineVisibleEntity::getSortCode).orderByDesc(FlowEngineVisibleEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowEngineVisibleEntity> getVisibleFlowList(String userId) {
        List<String> userList = new ArrayList<>();
        userList.add(userId);
        List<UserRelationEntity> list = serviceUtil.getListByUserIdAll(userList);
        List<String> userRelationList = list.stream().map(u -> u.getObjectId()).collect(Collectors.toList());
        userRelationList.add(userId);
        QueryWrapper<FlowEngineVisibleEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(FlowEngineVisibleEntity::getOperatorId,userRelationList);
        List<FlowEngineVisibleEntity> flowList  = this.list(wrapper);
        return flowList;
    }
}
