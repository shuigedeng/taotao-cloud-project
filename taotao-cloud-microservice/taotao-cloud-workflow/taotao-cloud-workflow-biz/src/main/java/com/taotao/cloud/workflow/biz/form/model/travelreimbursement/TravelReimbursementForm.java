package com.taotao.cloud.workflow.biz.form.model.travelreimbursement;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class TravelReimbursementForm {
    @ApiModelProperty(value = "车船费")
    private Long  fare;
    @ApiModelProperty(value = "其他费用")
    private Long other;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "出差任务")
    private String businessMission;
    @ApiModelProperty(value = "出差人员")
    private String travelerUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "到达地")
    private String destination;
    @ApiModelProperty(value = "出差补助")
    private Long travelAllowance;
    @ApiModelProperty(value = "票据数")
    private String billsNum;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请人")
    private String applyUser;
    @ApiModelProperty(value = "故障报修费")
    private Long breakdownFee;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "回归日期")
    private Long returnDate;
    @ApiModelProperty(value = "合计")
    private Long total;
    @ApiModelProperty(value = "机票费")
    private Long planeTicket;
    @ApiModelProperty(value = "停车费")
    private Long parkingRate;
    @ApiModelProperty(value = "住宿费用")
    private Long getAccommodation;
    @ApiModelProperty(value = "报销金额")
    private Long reimbursementAmount;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "报销编码")
    private String reimbursementId;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "出发日期")
    private Long setOutDate;
    @ApiModelProperty(value = "补找金额")
    private Long sumOfMoney;
    @ApiModelProperty(value = "借款金额")
    private Long loanAmount;
    @ApiModelProperty(value = "车辆里程")
    private Long vehicleMileage;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "过路费")
    private Long roadFee;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请部门")
    private String departmental;
    @ApiModelProperty(value = "轨道交通费")
    private Long railTransit;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @ApiModelProperty(value = "餐补费用")
    private Long mealAllowance;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
