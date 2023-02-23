package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 行政赏罚单
 */
@Data
@TableName("wform_rewardpunishment")
public class RewardPunishmentEntity {
    /**
     * 主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 主键
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
     * 员工姓名
     */
    @TableField("F_FULLNAME")
    private String fullName;

    /**
     * 填表日期
     */
    @TableField("F_FILLFROMDATE")
    private Date fillFromDate;

    /**
     * 员工部门
     */
    @TableField("F_DEPARTMENT")
    private String department;

    /**
     * 员工职位
     */
    @TableField("F_POSITION")
    private String position;

    /**
     * 赏罚金额
     */
    @TableField("F_REWARDPUN")
    private BigDecimal rewardPun;

    /**
     * 赏罚原因
     */
    @TableField("F_REASON")
    private String reason;
}
