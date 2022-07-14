package com.taotao.cloud.workflow.biz.form.model.paymentapply;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
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
public class PaymentApplyInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用途名称")
    private String purposeName;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "结算方式")
    private String settlementMethod;
    @ApiModelProperty(value = "开户银行")
    private String openingBank;
    @ApiModelProperty(value = "申请金额")
    private BigDecimal applyAmount;
    @ApiModelProperty(value = "付款类型")
    private String paymentType;
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "联系方式")
    private String receivableContact;
    @ApiModelProperty(value = "付款金额")
    private BigDecimal amountPaid;
    @ApiModelProperty(value = "项目类别")
    private String projectCategory;
    @ApiModelProperty(value = "申请部门")
    private String departmental;
    @ApiModelProperty(value = "项目负责人")
    private String projectLeader;
    @ApiModelProperty(value = "收款账号")
    private String beneficiaryAccount;
    @ApiModelProperty(value = "付款单位")
    private String paymentUnit;
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
}
