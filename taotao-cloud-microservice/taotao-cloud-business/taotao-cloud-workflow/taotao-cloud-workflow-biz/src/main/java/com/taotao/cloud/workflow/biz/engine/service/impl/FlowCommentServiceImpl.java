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

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentPagination;
import com.taotao.cloud.workflow.biz.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowCommentMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowCommentService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/** 流程评论 */
@Service
public class FlowCommentServiceImpl extends ServiceImpl<FlowCommentMapper, FlowCommentEntity>
        implements FlowCommentService {

    @Override
    public List<FlowCommentEntity> getlist(FlowCommentPagination pagination) {
        QueryWrapper<FlowCommentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowCommentEntity::getTaskId, pagination.getTaskId());
        queryWrapper.lambda().orderByDesc(FlowCommentEntity::getCreatorTime);
        Page<FlowCommentEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<FlowCommentEntity> userIPage = this.page(page, queryWrapper);
        return pagination.setData(userIPage.getRecords(), page.getTotal());
    }

    @Override
    public FlowCommentEntity getInfo(String id) {
        QueryWrapper<FlowCommentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowCommentEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void create(FlowCommentEntity entity) {
        entity.setCreatorTime(new Date());
        entity.setCreatorUserId(SecurityUtils.getUserId());
        entity.setId(RandomUtil.uuId());
        this.save(entity);
    }

    @Override
    @DSTransactional
    public void update(String id, FlowCommentEntity entity) {
        entity.setId(id);
        this.updateById(entity);
    }

    @Override
    public void delete(FlowCommentEntity entity) {
        if (entity != null) {
            this.removeById(entity.getId());
        }
    }
}
