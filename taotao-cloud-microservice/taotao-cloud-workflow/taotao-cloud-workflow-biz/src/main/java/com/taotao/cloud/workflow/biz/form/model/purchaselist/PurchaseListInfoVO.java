package com.taotao.cloud.workflow.biz.form.model.purchaselist;


import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 日常物品采购清单
 *
 */
@Data
public class PurchaseListInfoVO {
    @Schema(description = "主键")
    private String id;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @Schema(description = "申请人")
    private String applyUser;
    @NotBlank(message = "必填")
    @Schema(description = "所在部门")
    private String departmental;
    @Schema(description = "供应商名称")
    private String vendorName;
    @Schema(description = "采购人员")
    private String buyer;
    @NotNull(message = "采购日期不能为空")
    @Schema(description = "采购日期")
    private Long purchaseDate;
    @Schema(description = "仓库")
    private String warehouse;
    @Schema(description = "联系方式")
    private String telephone;
    @NotBlank(message = "支付方式不能为空")
    @Schema(description = "支付方式")
    private String paymentMethod;
    @Schema(description = "支付总额")
    private BigDecimal paymentMoney;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "用途原因")
    private String reason;
    @Schema(description = "明细")
    List<PurchaseListEntryEntityInfoModel> entryList;
}
