package com.taotao.cloud.workflow.biz.form.model.salessupport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售支持表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class SalesSupportInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "售前支持")
    private String pSaleSupInfo;
    @ApiModelProperty(value = "结束时间")
    private Long  endDate;
    @ApiModelProperty(value = "相关项目")
    private String project;
    @ApiModelProperty(value = "交付说明")
    private String consultResult;
    @ApiModelProperty(value = "售前顾问")
    private String pSalSupConsul;
    @ApiModelProperty(value = "咨询评价")
    private String iEvaluation;
    @ApiModelProperty(value = "支持天数")
    private String pSaleSupDays;
    @ApiModelProperty(value = "发起人总结")
    private String conclusion;
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "准备天数")
    private String pSalePreDays;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "销售总结")
    private String salSupConclu;
    @ApiModelProperty(value = "申请部门")
    private String applyDept;
    @ApiModelProperty(value = "机构咨询")
    private String consulManager;
    @ApiModelProperty(value = "申请日期")
    private Long  applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "开始时间")
    private Long  startDate;
    @ApiModelProperty(value = "相关客户")
    private String customer;
}
