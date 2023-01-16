package com.taotao.cloud.workflow.api.common.model.engine.flowmonitor;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class FlowMonitorListVO {
    @Schema(description = "流程编码")
    private String enCode;
    @Schema(description = "发起人员id")
    private String creatorUserId;
    @Schema(description = "创建时间")
    private Long creatorTime;
    @Schema(description = "当前节点")
    private String thisStep;
    @Schema(description = "所属分类")
    private String flowCategory;
    @Schema(description = "流程标题")
    private String fullName;
    @Schema(description = "所属流程")
    private String flowName;
    @Schema(description = "流程状态", example = "1")
    private Integer status;
    @Schema(description = "开始时间")
    private Long startTime;
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程编码")
    private String flowCode;
    @Schema(description = "实例进程")
    private String processId;
    @Schema(description = "完成情况")
    private Integer completion;
    @Schema(description = "用户名称")
    private String userName;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @Schema(description = "表单数据")
    private String formData;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;

}
