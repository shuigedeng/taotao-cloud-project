package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineVisibleEntity;

import java.util.List;

/**
 * 流程可见
 *
 */
public interface FlowEngineVisibleService extends IService<FlowEngineVisibleEntity> {

    /**
     * 列表
     *
     * @param flowId 流程主键
     * @return
     */
    List<FlowEngineVisibleEntity> getList(String flowId);

    /**
     * 可见流程列表
     *
     * @param userId 用户主键
     * @return
     */
    List<FlowEngineVisibleEntity> getVisibleFlowList(String userId);
}
