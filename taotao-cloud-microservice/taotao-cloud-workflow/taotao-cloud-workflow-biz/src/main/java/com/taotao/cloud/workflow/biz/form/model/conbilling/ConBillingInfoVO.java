package com.taotao.cloud.workflow.biz.form.model.conbilling;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 合同开票流程
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ConBillingInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "开户账号")
    private String amount;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "关联名称")
    private String conName;
    @ApiModelProperty(value = "开票人")
    private String drawer;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "开票日期")
    private Long billDate;
    @ApiModelProperty(value = "发票地址")
    private String invoAddress;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "开户银行")
    private String bank;
    @ApiModelProperty(value = "开票金额")
    private BigDecimal billAmount;
    @ApiModelProperty(value = "付款金额")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "税号")
    private String taxId;
    @ApiModelProperty(value = "发票号")
    private String invoiceId;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;

}
