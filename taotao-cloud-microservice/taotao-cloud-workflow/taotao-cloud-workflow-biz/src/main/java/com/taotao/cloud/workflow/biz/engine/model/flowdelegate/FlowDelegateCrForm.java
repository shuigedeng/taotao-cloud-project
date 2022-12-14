package com.taotao.cloud.workflow.biz.engine.model.flowdelegate;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
public class FlowDelegateCrForm {
    @Schema(description = "流程分类")
    @NotBlank(message = "必填")
    private String flowCategory;
    @Schema(description = "被委托人")
    @NotBlank(message = "必填")
    private String toUserName;
    @Schema(description = "被委托人id")
    @NotBlank(message = "必填")
    private String toUserId;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "开始日期")
    @NotNull(message = "必填")
    private Long startTime;
    @Schema(description = "结束日期")
    @NotNull(message = "必填")
    private Long endTime;
    @Schema(description = "委托流程id")
    @NotBlank(message = "必填")
    private String flowId;
    @Schema(description = "委托流程名称")
    @NotBlank(message = "必填")
    private String flowName;
}
