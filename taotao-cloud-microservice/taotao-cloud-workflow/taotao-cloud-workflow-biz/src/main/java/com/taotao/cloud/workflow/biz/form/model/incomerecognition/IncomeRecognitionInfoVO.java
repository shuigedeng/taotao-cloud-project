package com.taotao.cloud.workflow.biz.form.model.incomerecognition;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 收入确认分析表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class IncomeRecognitionInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;
    @ApiModelProperty(value = "联系电话")
    private String contacPhone;
    @ApiModelProperty(value = "到款金额")
    private BigDecimal actualAmount;
    @ApiModelProperty(value = "到款银行")
    private String moneyBank;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "合同金额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "未付金额")
    private BigDecimal unpaidAmount;
    @ApiModelProperty(value = "已付金额")
    private BigDecimal amountPaid;
    @ApiModelProperty(value = "结算月份")
    private String settlementMonth;
    @ApiModelProperty(value = "合同编码")
    private String contractNum;
    @ApiModelProperty(value = "到款日期")
    private Long paymentDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "联系QQ")
    private String contactQQ;

}
