package com.taotao.cloud.workflow.biz.form.model.applymeeting;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
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
public class ApplyMeetingInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "查看人")
    private String lookPeople;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "会议名称")
    private String conferenceName;
    @ApiModelProperty(value = "结束日期")
    private Long endDate;
    @ApiModelProperty(value = "出席人")
    private String attendees;
    @ApiModelProperty(value = "纪要员")
    private String memo;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "会议类型")
    private String conferenceType;
    @ApiModelProperty(value = "会议室")
    private String conferenceRoom;
    @ApiModelProperty(value = "会议主题")
    private String conferenceTheme;
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
    @ApiModelProperty(value = "所属职务")
    private String position;
    @ApiModelProperty(value = "会议描述")
    private String describe;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "预计金额")
    private BigDecimal estimatedAmount;
    @ApiModelProperty(value = "开始日期")
    private Long startDate;
}
