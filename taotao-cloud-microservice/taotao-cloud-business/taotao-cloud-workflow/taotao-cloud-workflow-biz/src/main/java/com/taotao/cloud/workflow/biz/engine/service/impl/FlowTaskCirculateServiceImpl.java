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
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowTaskCirculateMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskCirculateService;
import java.util.List;
import org.springframework.stereotype.Service;

/** 流程传阅 */
@Service
public class FlowTaskCirculateServiceImpl extends ServiceImpl<FlowTaskCirculateMapper, FlowTaskCirculateEntity>
        implements FlowTaskCirculateService {

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
