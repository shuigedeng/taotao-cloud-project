package com.taotao.cloud.workflow.biz.form.model.applymeeting;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会议申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ApplyMeetingForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "查看人")
    private String lookPeople;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "会议名称不能为空")
    @ApiModelProperty(value = "会议名称")
    private String conferenceName;
    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期")
    private Long endDate;
    @ApiModelProperty(value = "出席人")
    private String attendees;
    @ApiModelProperty(value = "纪要员")
    private String memo;
    @NotBlank(message = "申请人员不能为空")
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "会议类型")
    private String conferenceType;
    @ApiModelProperty(value = "会议室")
    private String conferenceRoom;
    @ApiModelProperty(value = "会议主题")
    private String conferenceTheme;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "管理人")
    private String administrator;
    @ApiModelProperty(value = "其他出席人")
    private String otherAttendee;
    @ApiModelProperty(value = "预计人数")
    private String estimatePeople;
    @ApiModelProperty(value = "申请材料")
    private String applyMaterial;
    @NotBlank(message = "所属职务不能为空")
    @ApiModelProperty(value = "所属职务")
    private String position;
    @ApiModelProperty(value = "会议描述")
    private String describe;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "预计金额")
    private BigDecimal estimatedAmount;
    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期")
    private Long startDate;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
