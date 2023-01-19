package com.taotao.cloud.workflow.biz.common.model.engine.flowengine;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import lombok.Data;

/**
 */
@Data
public class FlowOperatordModel {
    /** 审批状态 */
    private Integer status;
    /** 审批原因 */
    private FlowModel flowModel;
    /** 审核人 */
    private String userId;
    /** 节点对象 */
    private FlowTaskOperatorEntity operator;
    /** 流转操作人 */
    private String operatorId;
}
