package com.taotao.cloud.workflow.biz.engine.model.flowbefore;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 */
@Data
public class FlowBeforeListVO {
    @Schema(description = "流程编码")
    private String enCode;
    @Schema(description = "发起人员")
    private String creatorUserId;
    @Schema(description = "接收时间")
    private Long creatorTime;
    @Schema(description = "经办节点")
    private String thisStep;
    @Schema(description = "节点id")
    private String thisStepId;
    @Schema(description = "所属分类")
    private String flowCategory;
    @Schema(description = "流程标题")
    private String fullName;
    @Schema(description = "所属流程")
    private String flowName;
    @Schema(description = "流程状态", example = "1")
    private Integer status;
    @Schema(description = "发起时间")
    private Long startTime;
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "用户名称")
    private String userName;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "流程编码")
    private String flowCode;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "实例进程")
    private String processId;
    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "节点")
    private String nodeName;
    @Schema(description = "节点对象")
    private String approversProperties;
    @Schema(description = "版本")
    private String flowVersion;

}
