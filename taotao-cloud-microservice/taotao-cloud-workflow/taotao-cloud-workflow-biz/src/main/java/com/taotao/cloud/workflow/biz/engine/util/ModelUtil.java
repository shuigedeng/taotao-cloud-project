package com.taotao.cloud.workflow.biz.engine.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jnpf.engine.model.flowengine.FlowModel;
import jnpf.engine.service.FlowTaskNewService;
import jnpf.exception.WorkFlowException;
import jnpf.util.JsonUtil;
import jnpf.util.context.SpringContext;

/**
 * 工作流
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/5/24 9:19
 */
public class ModelUtil {

    public static void save(String id, String flowId, String processId, String flowTitle, int flowUrgent, String billNo, Object formEntity) throws WorkFlowException {
        FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
        FlowModel flowModel = assignment(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, null, new HashMap<>());
        flowTaskNewService.saveIsAdmin(flowModel);
    }

    public static void submit(String id, String flowId, String processId, String flowTitle, int flowUrgent, String billNo, Object formEntity, String freeApproverUserId) throws WorkFlowException {
        submit(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, freeApproverUserId, new HashMap<>());
    }

    public static void submit(String id, String flowId, String processId, String flowTitle, int flowUrgent, String billNo, Object formEntity, String freeApproverUserId, Map<String, List<String>> candidateList) throws WorkFlowException {
        FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
        FlowModel flowModel = assignment(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, freeApproverUserId, candidateList);
        flowTaskNewService.submit(flowModel);
    }

    private static FlowModel assignment(String id, String flowId, String processId, String flowTitle, int flowUrgent, String billNo, Object formEntity, String freeApproverUserId, Map<String, List<String>> candidateList) {
        FlowModel flowModel = new FlowModel();
        flowModel.setId(id);
        flowModel.setFlowId(flowId);
        flowModel.setProcessId(processId);
        flowModel.setFlowTitle(flowTitle);
        flowModel.setFlowUrgent(flowUrgent);
        flowModel.setBillNo(billNo);
        Map<String, Object> data = JsonUtil.entityToMap(formEntity);
        flowModel.setFormData(data);
        flowModel.setFreeApproverUserId(freeApproverUserId);
        flowModel.setParentId(FlowNature.ParentId);
        flowModel.setIsAsync(false);
        flowModel.setCandidateList(candidateList);
        return flowModel;
    }

}
