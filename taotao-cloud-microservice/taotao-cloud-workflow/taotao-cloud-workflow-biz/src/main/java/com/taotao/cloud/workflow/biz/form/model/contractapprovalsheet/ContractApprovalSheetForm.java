package com.taotao.cloud.workflow.biz.form.model.contractapprovalsheet;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 合同申请单表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ContractApprovalSheetForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "支出总额")
    private BigDecimal totalExpenditure;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "结束时间")
    private Long endContractDate;
    @ApiModelProperty(value = "预算批付")
    private String budgetaryApproval;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "乙方")
    private String secondParty;
    @ApiModelProperty(value = "合同类型")
    private String contractType;
    @ApiModelProperty(value = "所属部门")
    private String leadDepartment;
    @ApiModelProperty(value = "收入金额")
    private BigDecimal incomeAmount;
    @ApiModelProperty(value = "内容简要")
    private String contractContent;
    @ApiModelProperty(value = "签订地区")
    private String signArea;
    @ApiModelProperty(value = "合同期限")
    private String contractPeriod;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "编码支出")
    private String contractId;
    @ApiModelProperty(value = "签署方(甲方)")
    private String firstParty;
    @ApiModelProperty(value = "合作负责人")
    private String personCharge;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "结算方式")
    private String paymentMethod;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "开始时间")
    private Long startContractDate;
    @ApiModelProperty(value = "合同号")
    private String contractNum;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
