package com.taotao.cloud.workflow.biz.form.model.travelreimbursement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 差旅报销申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class TravelReimbursementInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "车船费")
    private Long  fare;
    @ApiModelProperty(value = "其他费用")
    private Long  other;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "出差任务")
    private String businessMission;
    @ApiModelProperty(value = "")
    private String travelerUser;
    @ApiModelProperty(value = "到达地")
    private String destination;
    @ApiModelProperty(value = "出差补助")
    private Long  travelAllowance;
    @ApiModelProperty(value = "票据数")
    private String billsNum;
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "故障报修费")
    private Long  breakdownFee;
    @ApiModelProperty(value = "回归日期")
    private Long  returnDate;
    @ApiModelProperty(value = "合计")
    private Long  total;
    @ApiModelProperty(value = "机票费")
    private Long  planeTicket;
    @ApiModelProperty(value = "停车费")
    private Long  parkingRate;
    @ApiModelProperty(value = "住宿费用")
    private Long  getAccommodation;
    @ApiModelProperty(value = "报销金额")
    private Long  reimbursementAmount;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "报销编码")
    private String reimbursementId;
    @ApiModelProperty(value = "出发日期")
    private Long  setOutDate;
    @ApiModelProperty(value = "补找金额")
    private Long  sumOfMoney;
    @ApiModelProperty(value = "借款金额")
    private Long  loanAmount;
    @ApiModelProperty(value = "车辆里程")
    private Long  vehicleMileage;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "过路费")
    private Long  roadFee;
    @ApiModelProperty(value = "申请部门")
    private String departmental;
    @ApiModelProperty(value = "轨道交通费")
    private Long  railTransit;
    @ApiModelProperty(value = "申请时间")
    private Long  applyDate;
    @ApiModelProperty(value = "餐补费用")
    private Long  mealAllowance;
}
