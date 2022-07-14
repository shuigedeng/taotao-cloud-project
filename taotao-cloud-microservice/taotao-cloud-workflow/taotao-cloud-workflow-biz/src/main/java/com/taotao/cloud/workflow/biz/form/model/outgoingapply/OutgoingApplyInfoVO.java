package com.taotao.cloud.workflow.biz.form.model.outgoingapply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 外出申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class OutgoingApplyInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "目的地")
    private String destination;
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "发文标题")
    private String flowTitle;
    @ApiModelProperty(value = "外出事由")
    private String outgoingCause;
    @ApiModelProperty(value = "开始时间")
    private Long startTime;
    @ApiModelProperty(value = "结束时间")
    private Long endTime;
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @ApiModelProperty(value = "所在部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "外出总计")
    private String outgoingTotle;
}
