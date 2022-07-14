package com.taotao.cloud.workflow.biz.form.model.debitbill;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 借支单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class DebitBillInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "借款原因")
    private String reason;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "还款票据")
    private String repaymentBill;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "员工职务")
    private String staffPost;
    @ApiModelProperty(value = "还款日期")
    private Long teachingDate;
    @ApiModelProperty(value = "工作部门")
    private String departmental;
    @ApiModelProperty(value = "员工姓名")
    private String staffName;
    @ApiModelProperty(value = "借款方式")
    private String loanMode;
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "转账账户")
    private String transferAccount;
    @ApiModelProperty(value = "借支金额")
    private BigDecimal amountDebit;
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "员工编码")
    private String staffId;
}
