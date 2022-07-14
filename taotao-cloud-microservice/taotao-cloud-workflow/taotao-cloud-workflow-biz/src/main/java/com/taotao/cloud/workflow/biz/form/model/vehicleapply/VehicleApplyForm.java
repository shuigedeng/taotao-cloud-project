package com.taotao.cloud.workflow.biz.form.model.vehicleapply;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class VehicleApplyForm {
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotNull(message = "结束时间不能为空")
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
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "路费金额")
    private BigDecimal roadFee;
    @NotBlank(message = "所在部门不能为空")
    @ApiModelProperty(value = "所在部门")
    private String department;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotBlank(message = "用车人不能为空")
    @ApiModelProperty(value = "用车人")
    private String carMan;
    @ApiModelProperty(value = "随行人数")
    private String entourage;
    @NotNull(message = "用车日期不能为空")
    @ApiModelProperty(value = "用车日期")
    private Long  startDate;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
