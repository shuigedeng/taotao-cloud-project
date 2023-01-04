package com.taotao.cloud.workflow.api.common.model.engine.flowtask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowContModel {
    /**
     * 审批类型
     */
    private Integer type;
    /**
     * 编码
     */
    private String enCode;
    /**
     * 引擎id
     */
    private String flowId;
    /**
     * 表单分类
     */
    private Integer formType;
    /**
     * 任务id
     */
    private String processId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 代办id
     */
    private String taskOperatorId;
    /**
     * 节点id
     */
    private String taskNodeId;
}
