package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 员工加班申请表
 */
@Data
@TableName("wform_staffovertime")
public class StaffOvertimeEntity {
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
     * 申请部门
     */
    @TableField("F_DEPARTMENT")
    private String department;

    /**
     * 申请日期
     */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /**
     * 总计时间
     */
    @TableField("F_TOTLETIME")
    private String totleTime;

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
     * 记入类别
     */
    @TableField("F_CATEGORY")
    private String category;

    /**
     * 加班事由
     */
    @TableField("F_CAUSE")
    private String cause;
}
