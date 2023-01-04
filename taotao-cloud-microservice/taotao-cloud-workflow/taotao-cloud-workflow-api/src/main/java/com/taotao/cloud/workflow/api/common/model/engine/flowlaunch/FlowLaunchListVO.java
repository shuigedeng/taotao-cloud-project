package com.taotao.cloud.workflow.api.common.model.engine.flowlaunch;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowLaunchListVO {
    @Schema(description = "任务编码")
    private String enCode;
    @Schema(description = "发起人员")
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
    @Schema(description = "发起时间")
    private Long startTime;
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "结束时间")
    private Long endTime;
    @Schema(description = "完成情况")
    private Integer completion;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "流程编码")
    private String flowCode;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @Schema(description = "表单数据")
    private String formData;
}
