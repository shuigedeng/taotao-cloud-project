/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskOperatorMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 流程经办记录 */
@Service
public class FlowTaskOperatorServiceImpl extends ServiceImpl<FlowTaskOperatorMapper, FlowTaskOperatorEntity>
        implements FlowTaskOperatorService {

    @Autowired
    private FlowDelegateService flowDelegateService;

    @Override
    public List<FlowTaskOperatorEntity> getList(String taskId) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(FlowTaskOperatorEntity::getTaskId, taskId)
                .orderByAsc()
                .orderByDesc(FlowTaskOperatorEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public FlowTaskOperatorEntity getInfo(String id) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public FlowTaskOperatorEntity getInfo(String taskId, String nodeNo) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        UserInfo userInfo = userProvider.get();
        queryWrapper
                .lambda()
                .eq(FlowTaskOperatorEntity::getTaskId, taskId)
                .eq(FlowTaskOperatorEntity::getNodeCode, nodeNo)
                .eq(FlowTaskOperatorEntity::getHandleId, userInfo.getUserId())
                .eq(FlowTaskOperatorEntity::getCompletion, FlowNature.ProcessCompletion);
        return this.getOne(queryWrapper);
    }

    @Override
    public void deleteByTaskId(String taskId) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorEntity::getTaskId, taskId);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteByNodeId(String nodeId) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(FlowTaskOperatorEntity::getTaskNodeId, nodeId)
                .eq(FlowTaskOperatorEntity::getCompletion, FlowNature.ProcessCompletion);
        this.remove(queryWrapper);
    }

    @Override
    public void create(List<FlowTaskOperatorEntity> entitys) {
        for (FlowTaskOperatorEntity entity : entitys) {
            this.save(entity);
        }
    }

    @Override
    public void update(FlowTaskOperatorEntity entity) {
        this.updateById(entity);
    }

    @Override
    public void update(String taskNodeId, List<String> userId, String completion) {
        if (userId.size() > 0) {
            UpdateWrapper<FlowTaskOperatorEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(FlowTaskOperatorEntity::getTaskNodeId, taskNodeId);
            updateWrapper.lambda().in(FlowTaskOperatorEntity::getHandleId, userId);
            updateWrapper.lambda().set(FlowTaskOperatorEntity::getCompletion, FlowNature.AuditCompletion);
            this.update(updateWrapper);
        }
    }

    @Override
    public void update(String taskNodeId, String type) {
        UpdateWrapper<FlowTaskOperatorEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(FlowTaskOperatorEntity::getTaskNodeId, taskNodeId);
        updateWrapper.lambda().eq(FlowTaskOperatorEntity::getType, type);
        updateWrapper.lambda().set(FlowTaskOperatorEntity::getCompletion, FlowNature.AuditCompletion);
        this.update(updateWrapper);
    }

    @Override
    public void update(String taskId) {
        UpdateWrapper<FlowTaskOperatorEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(FlowTaskOperatorEntity::getTaskId, taskId);
        updateWrapper.lambda().set(FlowTaskOperatorEntity::getState, FlowNodeEnum.Futility.getCode());
        this.update(updateWrapper);
    }

    @Override
    public List<FlowTaskOperatorEntity> press(String taskId) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(FlowTaskOperatorEntity::getCompletion, FlowNature.ProcessCompletion)
                .eq(FlowTaskOperatorEntity::getTaskId, taskId)
                .eq(FlowTaskOperatorEntity::getState, FlowNodeEnum.Process.getCode());
        return this.list(queryWrapper);
    }

    @Override
    public void updateReject(String taskId, Set<String> taskNodeId) {
        if (taskNodeId.size() > 0) {
            UpdateWrapper<FlowTaskOperatorEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(FlowTaskOperatorEntity::getTaskId, taskId);
            updateWrapper.lambda().in(FlowTaskOperatorEntity::getTaskNodeId, taskNodeId);
            updateWrapper.lambda().set(FlowTaskOperatorEntity::getState, FlowNodeEnum.Futility.getCode());
            this.update(updateWrapper);
        }
    }

    @Override
    public void deleteList(List<String> idAll) {
        if (idAll.size() > 0) {
            QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowTaskOperatorEntity::getId, idAll);
            this.remove(queryWrapper);
        }
    }

    @Override
    public List<FlowTaskOperatorEntity> getParentId(String parentId) {
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorEntity::getParentId, parentId);
        queryWrapper.lambda().orderByDesc(FlowTaskOperatorEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public void updateTaskOperatorState(List<String> idAll) {
        if (idAll.size() > 0) {
            UpdateWrapper<FlowTaskOperatorEntity> queryWrapper = new UpdateWrapper<>();
            queryWrapper.lambda().in(FlowTaskOperatorEntity::getId, idAll);
            queryWrapper.lambda().set(FlowTaskOperatorEntity::getState, FlowNodeEnum.Futility.getCode());
            queryWrapper.lambda().set(FlowTaskOperatorEntity::getCompletion, FlowNature.RejectCompletion);
            this.update(queryWrapper);
        }
    }

    @Override
    public List<FlowTaskOperatorEntity> getBatchList() {
        UserInfo userInfo = userProvider.get();
        List<String> userList = flowDelegateService.getUser(userInfo.getUserId()).stream()
                .map(FlowDelegateEntity::getCreatorUserId)
                .toList();
        userList.add(userInfo.getUserId());
        QueryWrapper<FlowTaskOperatorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FlowTaskOperatorEntity::getHandleId, userList);
        queryWrapper.lambda().eq(FlowTaskOperatorEntity::getCompletion, FlowNature.ProcessCompletion);
        queryWrapper.lambda().eq(FlowTaskOperatorEntity::getState, FlowNature.ProcessCompletion);
        queryWrapper.lambda().select(FlowTaskOperatorEntity::getTaskId);
        return list(queryWrapper);
    }
}
