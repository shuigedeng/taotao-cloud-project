package com.taotao.cloud.workflow.biz.form.model.warehousereceipt;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 入库申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class WarehouseReceiptForm{
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
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "入库类别")
    private String warehousCategory;
    @ApiModelProperty(value = "仓库")
    private String warehouse;
    @ApiModelProperty(value = "入库人")
    private String warehousesPeople;
    @ApiModelProperty(value = "送货单号")
    private String deliveryNo;
    @ApiModelProperty(value = "入库单号")
    private String warehouseNo;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "入库日期")
    private Long warehousDate;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "明细")
    List<WarehouseReceiptEntityInfoModel> entryList;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
