package com.taotao.cloud.workflow.biz.form.model.incomerecognition;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class IncomeRecognitionForm {
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "联系电话")
    private String contacPhone;
    @ApiModelProperty(value = "到款金额")
    private BigDecimal actualAmount;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "到款银行")
    private String moneyBank;
    @ApiModelProperty(value = "备注")
    private String description;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "合同总金额")
    private BigDecimal totalAmount;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "未付金额")
    private BigDecimal unpaidAmount;
    @ApiModelProperty(value = "已付金额")
    private BigDecimal amountPaid;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "结算月份")
    private String settlementMonth;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "合同编码")
    private String contractNum;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "到款日期")
    private Long paymentDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "联系QQ")
    private String contactQQ;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
