package com.taotao.cloud.workflow.biz.engine.service;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskForm;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskInfoVO;

import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.Map;

/**
 * 在线开发工作流
 *
 */
public interface FlowDynamicService {

    /**
     * 表单信息
     *
     * @param entity 流程任务对象
     * @return
     * @throws WorkFlowException 异常
     */
    FlowTaskInfoVO info(FlowTaskEntity entity, String taskOperatorId) throws WorkFlowException;

    /**
     * 保存
     *
     * @param flowTaskForm 对象
     * @throws WorkFlowException 异常
     */
    void save(String id, FlowTaskForm flowTaskForm) throws WorkFlowException;

    /**
     * 提交
     *
     * @param flowTaskForm 对象
     * @throws WorkFlowException 异常
     */
    void submit(String id,FlowTaskForm flowTaskForm) throws WorkFlowException;

    /**
     * 关联表单
     *
     * @param flowId 引擎id
     * @param id     数据id
     * @return
     * @throws WorkFlowException 异常
     */
    Map<String, Object> getData(String flowId, String id) throws WorkFlowException;
}
