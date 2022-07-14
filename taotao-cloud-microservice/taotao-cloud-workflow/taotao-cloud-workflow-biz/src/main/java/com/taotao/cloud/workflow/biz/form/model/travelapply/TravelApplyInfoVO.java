package com.taotao.cloud.workflow.biz.form.model.travelapply;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 出差预支申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class TravelApplyInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "起始地点")
    private String startPlace;
    @ApiModelProperty(value = "出差人")
    private String travelMan;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "结束日期")
    private Long endDate;
    @ApiModelProperty(value = "预支旅费")
    private BigDecimal prepaidTravel;
    @ApiModelProperty(value = "目的地")
    private String destination;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "所属部门")
    private String departmental;
    @ApiModelProperty(value = "所属职务")
    private String position;
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "开始日期")
    private Long startDate;
}
