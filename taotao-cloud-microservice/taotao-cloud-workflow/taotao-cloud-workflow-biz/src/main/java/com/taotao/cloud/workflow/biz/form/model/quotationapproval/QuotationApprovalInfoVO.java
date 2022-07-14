package com.taotao.cloud.workflow.biz.form.model.quotationapproval;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报价审批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class QuotationApprovalInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "合作人名")
    private String partnerName;
    @ApiModelProperty(value = "填表日期")
    private Long  writeDate;
    @ApiModelProperty(value = "情况描述")
    private String custSituation;
    @ApiModelProperty(value = "填报人")
    private String writer;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "类型")
    private String quotationType;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "模板参考")
    private String standardFile;
}
