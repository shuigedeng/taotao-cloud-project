package com.taotao.cloud.workflow.biz.form.model.salessupport;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class SalesSupportForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "售前支持")
    private String pSaleSupInfo;
    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间")
    private Long  endDate;
    @NotBlank(message = "相关项目不能为空")
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
    @NotBlank(message = "申请人不能为空")
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "准备天数")
    private String pSalePreDays;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "销售总结")
    private String salSupConclu;
    @NotBlank(message = "申请部门不能为空")
    @ApiModelProperty(value = "申请部门")
    private String applyDept;
    @ApiModelProperty(value = "机构咨询")
    private String consulManager;
    @NotNull(message = "申请日期不能为空")
    @ApiModelProperty(value = "申请日期")
    private Long  applyDate;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间")
    private Long  startDate;
    @NotBlank(message = "相关客户不能为空")
    @ApiModelProperty(value = "相关客户")
    private String customer;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
