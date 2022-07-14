package com.taotao.cloud.workflow.biz.engine.service;

import java.util.Map;
import jnpf.engine.entity.FlowTaskEntity;
import jnpf.engine.model.flowtask.FlowTaskForm;
import jnpf.engine.model.flowtask.FlowTaskInfoVO;
import jnpf.exception.WorkFlowException;

/**
 * 在线开发工作流
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:19
 */
public interface FlowDynamicService {

    /**
     * 表单信息
     *
     * @param entity 流程任务对象
     * @return
     * @throws WorkFlowException 异常
     */
    FlowTaskInfoVO info(FlowTaskEntity entity,String taskOperatorId) throws WorkFlowException;

    /**
     * 保存
     *
     * @param flowTaskForm 对象
     * @throws WorkFlowException 异常
     */
    void save(String id,FlowTaskForm flowTaskForm) throws WorkFlowException;

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
