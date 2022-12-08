package com.taotao.cloud.workflow.api.model.home;

import lombok.Data;

@Data
public class FlowTodoVO {

	public String id;

	public String fullName;

	public String enCode;

	public String flowId;

	public Integer formType;

	public Integer status;

	public String processId;

	public String taskNodeId;

	public String taskOperatorId;

	public Long creatorTime;

	public Integer type;


}
