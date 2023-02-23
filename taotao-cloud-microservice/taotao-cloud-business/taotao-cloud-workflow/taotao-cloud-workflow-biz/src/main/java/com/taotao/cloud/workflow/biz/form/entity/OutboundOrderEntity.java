package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 出库单
 */
@Data
@TableName("wform_outboundorder")
public class OutboundOrderEntity {
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
     * 客户名称
     */
    @TableField("F_CUSTOMERNAME")
    private String customerName;

    /**
     * 仓库
     */
    @TableField("F_WAREHOUSE")
    private String warehouse;

    /**
     * 仓库人
     */
    @TableField("F_OUTSTORAGE")
    private String outStorage;

    /**
     * 业务人员
     */
    @TableField("F_BUSINESSPEOPLE")
    private String businessPeople;

    /**
     * 业务类型
     */
    @TableField("F_BUSINESSTYPE")
    private String businessType;

    /**
     * 出库日期
     */
    @TableField("F_OUTBOUNDDATE")
    private Date outboundDate;

    /**
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;
}
