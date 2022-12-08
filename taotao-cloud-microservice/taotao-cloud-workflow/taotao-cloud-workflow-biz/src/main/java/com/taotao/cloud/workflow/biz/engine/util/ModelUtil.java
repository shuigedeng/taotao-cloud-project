package com.taotao.cloud.workflow.biz.engine.util;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.model.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流
 */
public class ModelUtil {

	public static void save(String id, String flowId, String processId, String flowTitle,
		int flowUrgent, String billNo, Object formEntity) throws WorkFlowException {
		FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
		FlowModel flowModel = assignment(id, flowId, processId, flowTitle, flowUrgent, billNo,
			formEntity, null, new HashMap<>());
		flowTaskNewService.saveIsAdmin(flowModel);
	}

	public static void submit(String id, String flowId, String processId, String flowTitle,
		int flowUrgent, String billNo, Object formEntity, String freeApproverUserId)
		throws WorkFlowException {
		submit(id, flowId, processId, flowTitle, flowUrgent, billNo, formEntity, freeApproverUserId,
			new HashMap<>());
	}

	public static void submit(String id, String flowId, String processId, String flowTitle,
		int flowUrgent, String billNo, Object formEntity, String freeApproverUserId,
		Map<String, List<String>> candidateList) throws WorkFlowException {
		FlowTaskNewService flowTaskNewService = SpringContext.getBean(FlowTaskNewService.class);
		FlowModel flowModel = assignment(id, flowId, processId, flowTitle, flowUrgent, billNo,
			formEntity, freeApproverUserId, candidateList);
		flowTaskNewService.submit(flowModel);
	}

	private static FlowModel assignment(String id, String flowId, String processId,
		String flowTitle, int flowUrgent, String billNo, Object formEntity,
		String freeApproverUserId, Map<String, List<String>> candidateList) {
		FlowModel flowModel = new FlowModel();
		flowModel.setId(id);
		flowModel.setFlowId(flowId);
		flowModel.setProcessId(processId);
		flowModel.setFlowTitle(flowTitle);
		flowModel.setFlowUrgent(flowUrgent);
		flowModel.setBillNo(billNo);
		Map<String, Object> data = JsonUtils.toMap(formEntity);
		flowModel.setFormData(data);
		flowModel.setFreeApproverUserId(freeApproverUserId);
		flowModel.setParentId(FlowNature.ParentId);
		flowModel.setIsAsync(false);
		flowModel.setCandidateList(candidateList);
		return flowModel;
	}

}
