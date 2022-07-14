package com.taotao.cloud.workflow.biz.form.model.receiptsign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收文签呈单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ReceiptSignInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "收文标题")
    private String receiptTitle;
    @ApiModelProperty(value = "收文日期")
    private Long receiptDate;
    @ApiModelProperty(value = "收文部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "收文简述")
    private String receiptPaper;
    @ApiModelProperty(value = "收文人")
    private String collector;
}
