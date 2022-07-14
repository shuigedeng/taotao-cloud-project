package com.taotao.cloud.workflow.biz.form.model.leaveapply;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 请假申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class LeaveApplyForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "请假天数")
    private String leaveDayCount;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "请假小时")
    private String leaveHour;
    @NotNull(message = "请假时间不能为空")
    @ApiModelProperty(value = "请假时间")
    private Long leaveStartTime;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请职位")
    private String applyPost;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请部门")
    private String applyDept;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "请假类别")
    private String leaveType;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "请假原因")
    private String leaveReason;
    @NotNull(message = "申请日期不能为空")
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间")
    private Long leaveEndTime;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
