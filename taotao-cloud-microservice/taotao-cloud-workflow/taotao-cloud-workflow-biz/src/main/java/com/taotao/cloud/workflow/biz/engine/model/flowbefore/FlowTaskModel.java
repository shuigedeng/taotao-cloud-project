package com.taotao.cloud.workflow.biz.engine.model.flowbefore;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 *
 */
@Data
public class FlowTaskModel {
    @Schema(description = "任务主键")
    private String id;
    @Schema(description = "实例进程")
    private String processId;
    @Schema(description = "任务编码")
    private String enCode;
    @Schema(description = "任务标题")
    private String fullName;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程编码")
    private String flowCode;
    @Schema(description = "流程名称")
    private String flowName;
    @Schema(description = "流程类型")
    private Integer flowType;
    @Schema(description = "流程分类")
    private String flowCategory;
    @Schema(description = "流程表单")
    private String flowForm;
    @Schema(description = "表单内容")
    private String flowFormContentJson;
    @Schema(description = "流程模板")
    private String flowTemplateJson;
    @Schema(description = "流程版本")
    private String flowVersion;
    @Schema(description = "开始时间")
    private Long  startTime;
    @Schema(description = "结束时间")
    private Long  endTime;
    @Schema(description = "当前步骤")
    private String thisStep;
    @Schema(description = "当前步骤Id")
    private String thisStepId;
    @Schema(description = "重要等级")
    private String grade;
    @Schema(description = "任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止")
    private Integer status;
    @Schema(description = "完成情况")
    private Integer completion;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "排序码")
    private Long  sortCode;
    @Schema(description = "有效标志")
    private Integer enabledMark;
    @Schema(description = "app表单路径")
    private String appFormUrl;
    @Schema(description = "pc表单路径")
    private String formUrl;
    @Schema(description = "流程类型")
    private Integer type;

}
