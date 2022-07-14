package com.taotao.cloud.workflow.biz.form.model.salesorder;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 销售订单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class SalesOrderInfoVO {
    @ApiModelProperty(value = "主键")
    private String id;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "流程等级不能为空")
    @ApiModelProperty(value = "流程等级")
    private Integer flowUrgent;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "业务人员")
    private String salesman;
    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "客户地址")
    private String customerAddres;
    @ApiModelProperty(value = "发票编码")
    private String ticketNum;
    @NotBlank(message = "开票日期不能为空")
    @ApiModelProperty(value = "开票日期")
    private Long ticketDate;
    @ApiModelProperty(value = "发票类型")
    private String invoiceType;
    @ApiModelProperty(value = "付款方式")
    private String paymentMethod;
    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentMoney;
    @ApiModelProperty(value = "销售日期")
    private Long salesDate;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "明细")
    List<SalesOrderEntryEntityInfoModel> entryList;
}
