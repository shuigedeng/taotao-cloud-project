package com.taotao.cloud.workflow.biz.form.model.purchaselist;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 日常物品采购清单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class PurchaseListInfoVO {
    @ApiModelProperty(value = "主键")
    private String id;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "所在部门")
    private String departmental;
    @ApiModelProperty(value = "供应商名称")
    private String vendorName;
    @ApiModelProperty(value = "采购人员")
    private String buyer;
    @NotNull(message = "采购日期不能为空")
    @ApiModelProperty(value = "采购日期")
    private Long purchaseDate;
    @ApiModelProperty(value = "仓库")
    private String warehouse;
    @ApiModelProperty(value = "联系方式")
    private String telephone;
    @NotBlank(message = "支付方式不能为空")
    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;
    @ApiModelProperty(value = "支付总额")
    private BigDecimal paymentMoney;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "用途原因")
    private String reason;
    @ApiModelProperty(value = "明细")
    List<PurchaseListEntryEntityInfoModel> entryList;
}
