package com.taotao.cloud.workflow.api.common.model.engine.flowbefore;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowTaskOperatorRecordModel {
    @Schema(description = "节点流转主键")
    private String id;
    @Schema(description = "节点编码")
    private String nodeCode;
    @Schema(description = "节点名称")
    private String nodeName;
    @Schema(description = "经办状态 0-拒绝、1-同意、2-提交、3-撤回、4-终止")
    private Integer handleStatus;
    @Schema(description = "经办人员")
    private String handleId;
    @Schema(description = "经办时间")
    private Long handleTime;
    @Schema(description = "经办理由")
    private String handleOpinion;
    @Schema(description = "经办主键")
    private String taskOperatorId;
    @Schema(description = "节点主键")
    private String taskNodeId;
    @Schema(description = "任务主键")
    private String taskId;
    @Schema(description = "用户名称")
    private String userName;
    @Schema(description = "签名")
    private String signImg;
    @Schema(description = "判断加签人")
    private Integer status;
    @Schema(description = "流转操作人")
    private String operatorId;
}
