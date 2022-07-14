package com.taotao.cloud.workflow.biz.form.model.conbilling;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class ConBillingForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "开户账号")
    private String amount;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "关联名称")
    private String conName;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "开票人")
    private String drawer;
    @ApiModelProperty(value = "备注")
    private String description;
    @NotNull(message = "开票日期不能为空")
    @ApiModelProperty(value = "开票日期")
    private Long billDate;
    @ApiModelProperty(value = "发票地址")
    private String invoAddress;
    @NotBlank(message = "必填")
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
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
