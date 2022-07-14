package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 违章处理申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_violationhandling")
public class ViolationHandlingEntity {
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
     * 车牌号
     */
    @TableField("F_PLATENUM")
    private String plateNum;

    /**
     * 驾驶人
     */
    @TableField("F_DRIVER")
    private String driver;

    /**
     * 负责人
     */
    @TableField("F_LEADINGOFFICIAL")
    private String leadingOfficial;

    /**
     * 违章日期
     */
    @TableField("F_PECCANCYDATE")
    private Date peccancyDate;

    /**
     * 通知日期
     */
    @TableField("F_NOTICEDATE")
    private Date noticeDate;

    /**
     * 限处理日期
     */
    @TableField("F_LIMITDATE")
    private Date limitDate;

    /**
     * 违章地点
     */
    @TableField("F_VIOLATIONSITE")
    private String violationSite;

    /**
     * 违章行为
     */
    @TableField("F_VIOLATIONBEHAVIOR")
    private String violationBehavior;

    /**
     * 违章扣分
     */
    @TableField("F_DEDUCTION")
    private String deduction;

    /**
     * 违章罚款
     */
    @TableField("F_AMOUNTMONEY")
    private BigDecimal amountMoney;

    /**
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;
}
