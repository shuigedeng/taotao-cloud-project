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
import com.taotao.cloud.workflow.biz.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowRecordEnum;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskOperatorRecordMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorRecordService;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/** 流程经办 */
@Service
public class FlowTaskOperatorRecordServiceImpl
        extends ServiceImpl<FlowTaskOperatorRecordMapper, FlowTaskOperatorRecordEntity>
        implements FlowTaskOperatorRecordService {

    @Override
    public List<FlowTaskOperatorRecordEntity> getList(String taskId) {
        QueryWrapper<FlowTaskOperatorRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(FlowTaskOperatorRecordEntity::getTaskId, taskId)
                .orderByAsc(FlowTaskOperatorRecordEntity::getHandleTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTaskOperatorRecordEntity> getRecordList(String taskId, List<Integer> handleStatus) {
        QueryWrapper<FlowTaskOperatorRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, taskId);
        if (!handleStatus.isEmpty()) {
            queryWrapper.lambda().in(FlowTaskOperatorRecordEntity::getHandleStatus, handleStatus);
        }
        queryWrapper.lambda().orderByDesc(FlowTaskOperatorRecordEntity::getHandleTime);
        return this.list(queryWrapper);
    }

    @Override
    public FlowTaskOperatorRecordEntity getInfo(String id) {
        QueryWrapper<FlowTaskOperatorRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void delete(FlowTaskOperatorRecordEntity entity) {
        QueryWrapper<FlowTaskOperatorRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getId, entity.getId());
        this.remove(queryWrapper);
    }

    @Override
    public void create(FlowTaskOperatorRecordEntity entity) {
        entity.setId(RandomUtil.uuId());
        this.save(entity);
    }

    @Override
    public void update(String id, FlowTaskOperatorRecordEntity entity) {
        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void updateStatus(Set<String> taskNodeId, String taskId) {
        if (!taskNodeId.isEmpty()) {
            UpdateWrapper<FlowTaskOperatorRecordEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(FlowTaskOperatorRecordEntity::getTaskNodeId, taskNodeId);
            updateWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, taskId);
            updateWrapper.lambda().set(FlowTaskOperatorRecordEntity::getStatus, FlowRecordEnum.revoke.getCode());
            this.update(updateWrapper);
        }
    }

    @Override
    public FlowTaskOperatorRecordEntity getInfo(String taskId, String taskNodeId, String taskOperatorId) {
        QueryWrapper<FlowTaskOperatorRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, taskId);
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskNodeId, taskNodeId);
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskOperatorId, taskOperatorId);
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getStatus, FlowNodeEnum.FreeApprover.getCode());
        queryWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getHandleStatus, FlowRecordEnum.audit.getCode());
        return this.getOne(queryWrapper);
    }

    @Override
    public void updateStatus(List<String> idAll) {
        if (!idAll.isEmpty()) {
            UpdateWrapper<FlowTaskOperatorRecordEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(FlowTaskOperatorRecordEntity::getId, idAll);
            updateWrapper.lambda().set(FlowTaskOperatorRecordEntity::getStatus, FlowRecordEnum.revoke.getCode());
            this.update(updateWrapper);
        }
    }

    @Override
    public void update(String taskId) {
        UpdateWrapper<FlowTaskOperatorRecordEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(FlowTaskOperatorRecordEntity::getTaskId, taskId);
        updateWrapper.lambda().set(FlowTaskOperatorRecordEntity::getStatus, FlowRecordEnum.revoke.getCode());
        this.update(updateWrapper);
    }
}
