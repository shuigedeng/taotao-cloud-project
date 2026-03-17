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

package com.taotao.cloud.workflow.biz.infrastructure.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.workflow.api.vo.UserRelationEntity;
import com.taotao.cloud.workflow.biz.infrastructure.engine.entity.FlowEngineVisibleEntity;
import com.taotao.cloud.workflow.biz.infrastructure.engine.mapper.FlowEngineVisibleMapper;
import com.taotao.cloud.workflow.biz.infrastructure.engine.service.FlowEngineVisibleService;
import com.taotao.cloud.workflow.biz.infrastructure.engine.util.ServiceAllUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** 流程可见 */
@Service
public class FlowEngineVisibleServiceImpl extends ServiceImpl<FlowEngineVisibleMapper, FlowEngineVisibleEntity>
        implements FlowEngineVisibleService {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Override
    public List<FlowEngineVisibleEntity> getList(String flowId) {
        QueryWrapper<FlowEngineVisibleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowEngineVisibleEntity::getFlowId, flowId);
        queryWrapper
                .lambda()
                .orderByAsc(FlowEngineVisibleEntity::getSortCode)
                .orderByDesc(FlowEngineVisibleEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowEngineVisibleEntity> getVisibleFlowList(String userId) {
        List<String> userList = new ArrayList<>();
        userList.add(userId);
        List<UserRelationEntity> list = serviceUtil.getListByUserIdAll(userList);
        List<String> userRelationList = list.stream().map(u -> u.getObjectId()).toList();
        userRelationList.add(userId);
        QueryWrapper<FlowEngineVisibleEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(FlowEngineVisibleEntity::getOperatorId, userRelationList);
        List<FlowEngineVisibleEntity> flowList = this.list(wrapper);
        return flowList;
    }
}
