package com.taotao.cloud.workflow.biz.form.model.workcontactsheet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作联系单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class WorkContactSheetInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "收件部门")
    private String serviceDepartment;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "收件人")
    private String recipients;
    @ApiModelProperty(value = "发件日期")
    private Long toDate;
    @ApiModelProperty(value = "发件人")
    private String drawPeople;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "收件日期")
    private Long collectionDate;
    @ApiModelProperty(value = "发件部门")
    private String issuingDepartment;
    @ApiModelProperty(value = "协调事项")
    private String coordination;

}
