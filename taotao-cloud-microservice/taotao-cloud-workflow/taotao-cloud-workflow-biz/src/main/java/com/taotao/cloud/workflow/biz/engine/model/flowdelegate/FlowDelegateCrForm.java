package com.taotao.cloud.workflow.biz.engine.model.flowdelegate;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class FlowDelegateCrForm {
    @ApiModelProperty(value = "流程分类")
    @NotBlank(message = "必填")
    private String flowCategory;
    @ApiModelProperty(value = "被委托人")
    @NotBlank(message = "必填")
    private String toUserName;
    @ApiModelProperty(value = "被委托人id")
    @NotBlank(message = "必填")
    private String toUserId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "开始日期")
    @NotNull(message = "必填")
    private Long startTime;
    @ApiModelProperty(value = "结束日期")
    @NotNull(message = "必填")
    private Long endTime;
    @ApiModelProperty(value = "委托流程id")
    @NotBlank(message = "必填")
    private String flowId;
    @ApiModelProperty(value = "委托流程名称")
    @NotBlank(message = "必填")
    private String flowName;
}
