package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 外出申请单
 */
@Data
@TableName("wform_outgoingapply")
public class OutgoingApplyEntity {
	/**
	 * 主键
	 */
	@TableId("F_ID")
	private String id;

	/**
	 * 流程主键
	 */
	@TableField("F_FLOWID")
	private String flowId;

	/**
	 * 流程标题
	 */
	@TableField("F_FLOWTITLE")
	private String flowTitle;

	/**
	 * 紧急程度
	 */
	@TableField("F_FLOWURGENT")
	private Integer flowUrgent;

	/**
	 * 流程单据
	 */
	@TableField("F_BILLNO")
	private String billNo;

	/**
	 * 申请人
	 */
	@TableField("F_APPLYUSER")
	private String applyUser;

	/**
	 * 所在部门
	 */
	@TableField("F_DEPARTMENT")
	private String department;

	/**
	 * 申请日期
	 */
	@TableField("F_APPLYDATE")
	private Date applyDate;

	/**
	 * 外出总计
	 */
	@TableField("F_OUTGOINGTOTLE")
	private String outgoingTotle;

	/**
	 * 开始时间
	 */
	@TableField("F_STARTTIME")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@TableField("F_ENDTIME")
	private Date endTime;

	/**
	 * 目的地
	 */
	@TableField("F_DESTINATION")
	private String destination;

	/**
	 * 相关附件
	 */
	@TableField("F_FILEJSON")
	private String fileJson;

	/**
	 * 外出事由
	 */
	@TableField("F_OUTGOINGCAUSE")
	private String outgoingCause;
}
