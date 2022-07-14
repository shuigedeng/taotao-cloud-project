package com.taotao.cloud.workflow.biz.engine.model.flowbefore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:18
 */
@Data
public class FlowTaskModel {
    @ApiModelProperty(value = "任务主键")
    private String id;
    @ApiModelProperty(value = "实例进程")
    private String processId;
    @ApiModelProperty(value = "任务编码")
    private String enCode;
    @ApiModelProperty(value = "任务标题")
    private String fullName;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程编码")
    private String flowCode;
    @ApiModelProperty(value = "流程名称")
    private String flowName;
    @ApiModelProperty(value = "流程类型")
    private Integer flowType;
    @ApiModelProperty(value = "流程分类")
    private String flowCategory;
    @ApiModelProperty(value = "流程表单")
    private String flowForm;
    @ApiModelProperty(value = "表单内容")
    private String flowFormContentJson;
    @ApiModelProperty(value = "流程模板")
    private String flowTemplateJson;
    @ApiModelProperty(value = "流程版本")
    private String flowVersion;
    @ApiModelProperty(value = "开始时间")
    private Long  startTime;
    @ApiModelProperty(value = "结束时间")
    private Long  endTime;
    @ApiModelProperty(value = "当前步骤")
    private String thisStep;
    @ApiModelProperty(value = "当前步骤Id")
    private String thisStepId;
    @ApiModelProperty(value = "重要等级")
    private String grade;
    @ApiModelProperty(value = "任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止")
    private Integer status;
    @ApiModelProperty(value = "完成情况")
    private Integer completion;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "排序码")
    private Long  sortCode;
    @ApiModelProperty(value = "有效标志")
    private Integer enabledMark;
    @ApiModelProperty(value = "app表单路径")
    private String appFormUrl;
    @ApiModelProperty(value = "pc表单路径")
    private String formUrl;
    @ApiModelProperty(value = "流程类型")
    private Integer type;

}
