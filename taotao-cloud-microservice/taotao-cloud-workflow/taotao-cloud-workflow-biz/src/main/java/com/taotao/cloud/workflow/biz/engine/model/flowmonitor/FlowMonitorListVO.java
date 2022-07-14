package com.taotao.cloud.workflow.biz.engine.model.flowmonitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:17
 */
@Data
public class FlowMonitorListVO {
    @ApiModelProperty(value = "流程编码")
    private String enCode;
    @ApiModelProperty(value = "发起人员id")
    private String creatorUserId;
    @ApiModelProperty(value = "创建时间")
    private Long creatorTime;
    @ApiModelProperty(value = "当前节点")
    private String thisStep;
    @ApiModelProperty(value = "所属分类")
    private String flowCategory;
    @ApiModelProperty(value = "流程标题")
    private String fullName;
    @ApiModelProperty(value = "所属流程")
    private String flowName;
    @ApiModelProperty(value = "流程状态", example = "1")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private Long startTime;
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程编码")
    private String flowCode;
    @ApiModelProperty(value = "实例进程")
    private String processId;
    @ApiModelProperty(value = "完成情况")
    private Integer completion;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;
    @ApiModelProperty(value = "表单数据")
    private String formData;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;

}
