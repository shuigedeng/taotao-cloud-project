package com.taotao.cloud.workflow.biz.form.model.applybanquet;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 宴请申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ApplyBanquetInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "预计费用")
    private BigDecimal expectedCost;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "宴请人员")
    private String banquetPeople;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "人员总数")
    private String total;
    @ApiModelProperty(value = "所属职务")
    private String position;
    @ApiModelProperty(value = "宴请地点")
    private String place;
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "宴请人数")
    private String banquetNum;

}
