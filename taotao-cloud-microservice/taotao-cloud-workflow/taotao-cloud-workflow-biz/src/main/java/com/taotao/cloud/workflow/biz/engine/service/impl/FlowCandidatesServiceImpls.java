package com.taotao.cloud.workflow.biz.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import jnpf.engine.entity.FlowCandidatesEntity;
import jnpf.engine.mapper.FlowCandidatesMapper;
import jnpf.engine.service.FlowCandidatesService;
import jnpf.util.RandomUtil;
import org.springframework.stereotype.Service;

/**
 * 流程候选人
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 */
@Service
public class FlowCandidatesServiceImpls extends ServiceImpl<FlowCandidatesMapper, FlowCandidatesEntity> implements FlowCandidatesService {

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
