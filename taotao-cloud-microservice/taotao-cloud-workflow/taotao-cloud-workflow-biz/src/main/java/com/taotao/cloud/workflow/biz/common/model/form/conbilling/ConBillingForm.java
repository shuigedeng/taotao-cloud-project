package com.taotao.cloud.workflow.biz.common.model.form.conbilling;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 合同开票流程
 *
 */
@Data
public class ConBillingForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "开户账号")
    private String amount;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "公司名称")
    private String companyName;
    @Schema(description = "关联名称")
    private String conName;
    @NotBlank(message = "必填")
    @Schema(description = "开票人")
    private String drawer;
    @Schema(description = "备注")
    private String description;
    @NotNull(message = "开票日期不能为空")
    @Schema(description = "开票日期")
    private Long billDate;
    @Schema(description = "发票地址")
    private String invoAddress;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "开户银行")
    private String bank;
    @Schema(description = "开票金额")
    private BigDecimal billAmount;
    @Schema(description = "付款金额")
    private BigDecimal payAmount;
    @Schema(description = "税号")
    private String taxId;
    @Schema(description = "发票号")
    private String invoiceId;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
