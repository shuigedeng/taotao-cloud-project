package com.taotao.cloud.workflow.biz.form.model.paymentapply;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 付款申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class PaymentApplyForm {
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用途名称")
    private String purposeName;
    @ApiModelProperty(value = "备注")
    private String description;
    @NotBlank(message = "结算方式不能为空")
    @ApiModelProperty(value = "结算方式")
    private String settlementMethod;
    @NotBlank(message = "开户银行不能为空")
    @ApiModelProperty(value = "开户银行")
    private String openingBank;
    @ApiModelProperty(value = "申请金额")
    private BigDecimal applyAmount;
    @NotBlank(message = "付款类型不能为空")
    @ApiModelProperty(value = "付款类型")
    private String paymentType;
    @NotBlank(message = "申请人不能为空")
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "联系方式")
    private String receivableContact;
    @ApiModelProperty(value = "付款金额")
    private BigDecimal amountPaid;
    @ApiModelProperty(value = "项目类别")
    private String projectCategory;
    @NotBlank(message = "申请部门不能为空")
    @ApiModelProperty(value = "申请部门")
    private String departmental;
    @ApiModelProperty(value = "项目负责人")
    private String projectLeader;
    @ApiModelProperty(value = "收款账号")
    private String beneficiaryAccount;
    @ApiModelProperty(value = "付款单位")
    private String paymentUnit;
    @NotNull(message = "申请时间不能为空")
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
