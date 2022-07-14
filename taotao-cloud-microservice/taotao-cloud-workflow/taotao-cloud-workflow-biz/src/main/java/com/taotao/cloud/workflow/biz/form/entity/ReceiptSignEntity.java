package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 收文签呈单
 */
@Data
@TableName("wform_receiptsign")
public class ReceiptSignEntity {
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
     * 收文标题
     */
    @TableField("F_RECEIPTTITLE")
    private String receiptTitle;

    /**
     * 收文部门
     */
    @TableField("F_DEPARTMENT")
    private String department;

    /**
     * 收文人
     */
    @TableField("F_COLLECTOR")
    private String collector;

    /**
     * 收文日期
     */
    @TableField("F_RECEIPTDATE")
    private Date receiptDate;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 收文简述
     */
    @TableField("F_RECEIPTPAPER")
    private String receiptPaper;
}
