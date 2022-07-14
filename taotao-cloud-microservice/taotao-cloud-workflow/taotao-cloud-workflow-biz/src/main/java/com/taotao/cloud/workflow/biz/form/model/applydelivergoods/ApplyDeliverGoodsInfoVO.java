package com.taotao.cloud.workflow.biz.form.model.applydelivergoods;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发货申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ApplyDeliverGoodsInfoVO {
    @ApiModelProperty(value = "主键")
    private String id;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @ApiModelProperty(value ="客户名称")
    private String customerName;
    @ApiModelProperty(value ="联系人")
    private String contacts;
    @ApiModelProperty(value ="联系电话")
    private String contactPhone;
    @ApiModelProperty(value ="客户地址")
    private String customerAddres;
    @ApiModelProperty(value ="货品所属")
    private String goodsBelonged;
    @ApiModelProperty(value ="发货日期")
    private Long invoiceDate;
    @ApiModelProperty(value ="货运公司")
    private String freightCompany;
    @ApiModelProperty(value ="发货类型")
    private String deliveryType;
    @ApiModelProperty(value ="货运单号")
    private String rransportNum;
    @ApiModelProperty(value ="货运费")
    private BigDecimal freightCharges;
    @ApiModelProperty(value ="保险金额")
    private BigDecimal cargoInsurance;
    @ApiModelProperty(value ="备注")
    private String description;
    @ApiModelProperty(value ="发货金额")
    private BigDecimal invoiceValue;
    @ApiModelProperty(value = "明细")
    List<ApplyDeliverGoodsEntryInfoModel> entryList;
}
