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

package com.taotao.cloud.workflow.biz.engine.util;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 工作流 */
public class ModelUtil {

    public static void save(
            String id,
            String flowId,
            String processId,
            String flowTitle,
            int flowUrgent,
            String billNo,
            Object formEntity)
            throws WorkFlowException {
        FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
        FlowModel flowModel =
                assignment(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, null, new HashMap<>());
        flowTaskNewService.saveIsAdmin(flowModel);
    }

    public static void submit(
            String id,
            String flowId,
            String processId,
            String flowTitle,
            int flowUrgent,
            String billNo,
            Object formEntity,
            String freeApproverUserId)
            throws WorkFlowException {
        submit(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, freeApproverUserId, new HashMap<>());
    }

    public static void submit(
            String id,
            String flowId,
            String processId,
            String flowTitle,
            int flowUrgent,
            String billNo,
            Object formEntity,
            String freeApproverUserId,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
        FlowModel flowModel = assignment(
                id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, freeApproverUserId, candidateList);
        flowTaskNewService.submit(flowModel);
    }

    private static FlowModel assignment(
            String id,
            String flowId,
            String processId,
            String flowTitle,
            int flowUrgent,
            String billNo,
            Object formEntity,
            String freeApproverUserId,
            Map<String, List<String>> candidateList) {
        FlowModel flowModel = new FlowModel();
        flowModel.setId(id);
        flowModel.setFlowId(flowId);
        flowModel.setProcessId(processId);
        flowModel.setFlowTitle(flowTitle);
        flowModel.setFlowUrgent(flowUrgent);
        flowModel.setBillNo(billNo);
        Map<String, Object> data = JacksonUtils.toMap(formEntity);
        flowModel.setFormData(data);
        flowModel.setFreeApproverUserId(freeApproverUserId);
        flowModel.setParentId(FlowNature.ParentId);
        flowModel.setIsAsync(false);
        flowModel.setCandidateList(candidateList);
        return flowModel;
    }
}
