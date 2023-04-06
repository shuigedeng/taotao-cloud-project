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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.workflow.biz.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCandidatesEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowCandidatesMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowCandidatesService;
import java.util.List;
import org.springframework.stereotype.Service;

/** 流程候选人 */
@Service
public class FlowCandidatesServiceImpls extends ServiceImpl<FlowCandidatesMapper, FlowCandidatesEntity>
        implements FlowCandidatesService {

    @Override
    public List<FlowCandidatesEntity> getlist(String taskNodeId) {
        QueryWrapper<FlowCandidatesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowCandidatesEntity::getTaskNodeId, taskNodeId);
        return this.list(queryWrapper);
    }

    @Override
    public FlowCandidatesEntity getInfo(String id) {
        QueryWrapper<FlowCandidatesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowCandidatesEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(FlowCandidatesEntity entity) {
        entity.setId(RandomUtil.uuId());
        this.save(entity);
    }

    @Override
    public void create(List<FlowCandidatesEntity> list) {
        for (FlowCandidatesEntity entity : list) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
        }
    }

    @Override
    public void update(String id, FlowCandidatesEntity entity) {
        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void delete(FlowCandidatesEntity entity) {
        if (entity != null) {
            this.removeById(entity.getId());
        }
    }

    @Override
    public void deleteByTaskId(String taskId) {
        QueryWrapper<FlowCandidatesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowCandidatesEntity::getTaskId, taskId);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteTaskNodeId(List<String> taskNodeId) {
        if (taskNodeId.size() > 0) {
            QueryWrapper<FlowCandidatesEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowCandidatesEntity::getTaskNodeId, taskNodeId);
            this.remove(queryWrapper);
        }
    }

    @Override
    public void delete(List<String> taskNodeId, String handleId, String operatorId) {
        if (taskNodeId.size() > 0) {
            QueryWrapper<FlowCandidatesEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(FlowCandidatesEntity::getTaskNodeId, taskNodeId);
            queryWrapper.lambda().eq(FlowCandidatesEntity::getHandleId, handleId);
            queryWrapper.lambda().eq(FlowCandidatesEntity::getOperatorId, operatorId);
            this.remove(queryWrapper);
        }
    }
}
