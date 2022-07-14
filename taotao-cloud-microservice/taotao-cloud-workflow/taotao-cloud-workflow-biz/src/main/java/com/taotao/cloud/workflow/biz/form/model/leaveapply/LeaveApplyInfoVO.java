package com.taotao.cloud.workflow.biz.form.model.leaveapply;

import io.swagger.annotations.ApiModelProperty;
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
public class LeaveApplyInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "请假天数")
    private String leaveDayCount;
    @ApiModelProperty(value = "请假小时")
    private String leaveHour;
    @ApiModelProperty(value = "请假时间")
    private Long leaveStartTime;
    @ApiModelProperty(value = "申请职位")
    private String applyPost;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "申请部门")
    private String applyDept;
    @ApiModelProperty(value = "请假类别")
    private String leaveType;
    @ApiModelProperty(value = "请假原因")
    private String leaveReason;
    @ApiModelProperty(value = "申请日期")
    private Long applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "结束时间")
    private Long leaveEndTime;

}
