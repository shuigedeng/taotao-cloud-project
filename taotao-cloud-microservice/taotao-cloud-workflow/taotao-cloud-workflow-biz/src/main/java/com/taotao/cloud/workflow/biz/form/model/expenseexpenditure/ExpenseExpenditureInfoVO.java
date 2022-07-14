package com.taotao.cloud.workflow.biz.form.model.expenseexpenditure;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 费用支出单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ExpenseExpenditureInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "支付金额")
    private BigDecimal amountPayment;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "非合同支出")
    private String nonContract;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "合计费用")
    private BigDecimal total;
    @ApiModelProperty(value = "开户银行")
    private String accountOpeningBank;
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "合同编码")
    private String contractNum;
    @ApiModelProperty(value = "开户姓名")
    private String openAccount;
    @ApiModelProperty(value = "申请日期")
    private Long  applyDate;
    @ApiModelProperty(value = "所在部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
}
