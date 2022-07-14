package com.taotao.cloud.workflow.biz.form.model.archivalborrow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 档案借阅申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ArchivalBorrowInfoVO {
    @ApiModelProperty(value = "主键id",example = "1")
    private String id;
    @ApiModelProperty(value = "紧急程度",example = "1")
    private Integer flowUrgent;
    @ApiModelProperty(value = "申请原因")
    private String applyReason;
    @ApiModelProperty(value = "档案编码")
    private String archivesId;
    @ApiModelProperty(value = "借阅方式")
    private String borrowMode;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "归还时间")
    private Long returnDate;
    @ApiModelProperty(value = "档案名称")
    private String archivesName;
    @ApiModelProperty(value = "借阅部门")
    private String borrowingDepartment;
    @ApiModelProperty(value = "借阅时间")
    private Long borrowingDate;
    @ApiModelProperty(value = "档案属性")
    private String archivalAttributes;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
}
