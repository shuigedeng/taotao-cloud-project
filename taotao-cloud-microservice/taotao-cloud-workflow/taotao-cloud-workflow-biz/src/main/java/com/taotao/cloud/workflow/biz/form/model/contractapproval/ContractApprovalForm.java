package com.taotao.cloud.workflow.biz.form.model.contractapproval;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 合同审批
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ContractApprovalForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "乙方单位")
    private String secondPartyUnit;
    @ApiModelProperty(value = "合同分类")
    private String contractClass;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "甲方单位")
    private String firstPartyUnit;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "结束时间")
    private Long endDate;
    @ApiModelProperty(value = "合同类型")
    private String contractType;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "签约时间")
    private Long signingDate;
    @ApiModelProperty(value = "甲方负责人")
    private String firstPartyPerson;
    @ApiModelProperty(value = "收入金额")
    private BigDecimal incomeAmount;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "填写人员")
    private String inputPerson;
    @ApiModelProperty(value = "主要内容")
    private String primaryCoverage;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "甲方联系方式")
    private String firstPartyContact;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "合同编码")
    private String contractId;
    @ApiModelProperty(value = "乙方联系方式")
    private String secondPartyContact;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "业务人员")
    private String businessPerson;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "乙方负责人")
    private String secondPartyPerson;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "开始时间")
    private Long startDate;
    @ApiModelProperty(value = "审核人")
    private String freeApproverUserId;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
