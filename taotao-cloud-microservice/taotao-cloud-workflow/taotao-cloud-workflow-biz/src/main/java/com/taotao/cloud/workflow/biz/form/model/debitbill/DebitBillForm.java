package com.taotao.cloud.workflow.biz.form.model.debitbill;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class DebitBillForm {
    @ApiModelProperty(value = "借款原因")
    private String reason;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "还款票据")
    private String repaymentBill;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "员工职务")
    private String staffPost;
    @ApiModelProperty(value = "还款日期")
    private Long teachingDate;
    @ApiModelProperty(value = "工作部门")
    private String departmental;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "员工姓名")
    private String staffName;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "借款方式")
    private String loanMode;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "转账账户")
    private String transferAccount;
    @ApiModelProperty(value = "借支金额")
    private BigDecimal amountDebit;
    @NotNull(message = "申请日期不能为空")
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "员工编码")
    private String staffId;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
