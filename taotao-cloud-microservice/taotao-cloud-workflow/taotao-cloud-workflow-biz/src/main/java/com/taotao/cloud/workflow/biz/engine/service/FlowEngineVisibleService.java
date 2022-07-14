package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import jnpf.engine.entity.FlowEngineVisibleEntity;

/**
 * 流程可见
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
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
