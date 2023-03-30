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

package com.taotao.cloud.workflow.biz.engine.service;

import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.Map;

/** 在线开发工作流 */
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
    void submit(String id, FlowTaskForm flowTaskForm) throws WorkFlowException;

    /**
     * 关联表单
     *
     * @param flowId 引擎id
     * @param id 数据id
     * @return
     * @throws WorkFlowException 异常
     */
    Map<String, Object> getData(String flowId, String id) throws WorkFlowException;
}
