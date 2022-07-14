package com.taotao.cloud.workflow.biz.form.model.salessupport;


import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 销售支持表
 *
 */
@Data
public class SalesSupportForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "售前支持")
    private String pSaleSupInfo;
    @NotNull(message = "结束时间不能为空")
    @Schema(description = "结束时间")
    private Long  endDate;
    @NotBlank(message = "相关项目不能为空")
    @Schema(description = "相关项目")
    private String project;
    @Schema(description = "交付说明")
    private String consultResult;
    @Schema(description = "售前顾问")
    private String pSalSupConsul;
    @Schema(description = "咨询评价")
    private String iEvaluation;
    @Schema(description = "支持天数")
    private String pSaleSupDays;
    @Schema(description = "发起人总结")
    private String conclusion;
    @NotBlank(message = "申请人不能为空")
    @Schema(description = "申请人")
    private String applyUser;
    @Schema(description = "准备天数")
    private String pSalePreDays;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "销售总结")
    private String salSupConclu;
    @NotBlank(message = "申请部门不能为空")
    @Schema(description = "申请部门")
    private String applyDept;
    @Schema(description = "机构咨询")
    private String consulManager;
    @NotNull(message = "申请日期不能为空")
    @Schema(description = "申请日期")
    private Long  applyDate;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @NotNull(message = "开始时间不能为空")
    @Schema(description = "开始时间")
    private Long  startDate;
    @NotBlank(message = "相关客户不能为空")
    @Schema(description = "相关客户")
    private String customer;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
