package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;

import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowCommentMapper;
import com.taotao.cloud.workflow.biz.engine.model.flowcomment.FlowCommentPagination;
import com.taotao.cloud.workflow.biz.engine.service.FlowCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程评论
 *
 */
@Service
public class FlowCommentServiceImpl extends ServiceImpl<FlowCommentMapper, FlowCommentEntity> implements FlowCommentService {

    @Autowired
    private UserProvider userProvider;

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
        entity.setCreatorUserId(userProvider.get().getUserId());
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
