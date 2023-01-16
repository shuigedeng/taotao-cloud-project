package com.taotao.cloud.workflow.api.common.model.form.applydelivergoods;


import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发货申请单
 *
 */
@Data
public class ApplyDeliverGoodsInfoVO {
    @Schema(description = "主键")
    private String id;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @TagModelProperty(value ="客户名称")
    private String customerName;
    @TagModelProperty(value ="联系人")
    private String contacts;
    @TagModelProperty(value ="联系电话")
    private String contactPhone;
    @TagModelProperty(value ="客户地址")
    private String customerAddres;
    @TagModelProperty(value ="货品所属")
    private String goodsBelonged;
    @TagModelProperty(value ="发货日期")
    private Long invoiceDate;
    @TagModelProperty(value ="货运公司")
    private String freightCompany;
    @TagModelProperty(value ="发货类型")
    private String deliveryType;
    @TagModelProperty(value ="货运单号")
    private String rransportNum;
    @TagModelProperty(value ="货运费")
    private BigDecimal freightCharges;
    @TagModelProperty(value ="保险金额")
    private BigDecimal cargoInsurance;
    @TagModelProperty(value ="备注")
    private String description;
    @TagModelProperty(value ="发货金额")
    private BigDecimal invoiceValue;
    @Schema(description = "明细")
    List<ApplyDeliverGoodsEntryInfoModel> entryList;
}
