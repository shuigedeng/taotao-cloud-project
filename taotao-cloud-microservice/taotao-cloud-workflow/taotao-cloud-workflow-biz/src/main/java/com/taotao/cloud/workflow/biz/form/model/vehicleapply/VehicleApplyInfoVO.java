package com.taotao.cloud.workflow.biz.form.model.vehicleapply;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 车辆申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class VehicleApplyInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "结束时间")
    private Long  endDate;
    @ApiModelProperty(value = "目的地")
    private String destination;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "公里数")
    private String kilometreNum;
    @ApiModelProperty(value = "车牌号")
    private String plateNum;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "路费金额")
    private BigDecimal roadFee;
    @ApiModelProperty(value = "所在部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "用车人")
    private String carMan;
    @ApiModelProperty(value = "随行人数")
    private String entourage;
    @ApiModelProperty(value = "开始日期")
    private Long  startDate;
}
