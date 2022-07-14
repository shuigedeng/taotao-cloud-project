package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 档案借阅申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("wform_archivalborrow")
public class ArchivalBorrowEntity {
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
     * 申请人员
     */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /**
     * 借阅部门
     */
    @TableField("F_BORROWINGDEPARTMENT")
    private String borrowingDepartment;

    /**
     * 档案名称
     */
    @TableField("F_ARCHIVESNAME")
    private String archivesName;

    /**
     * 借阅时间
     */
    @TableField("F_BORROWINGDATE")
    private Date borrowingDate;

    /**
     * 归还时间
     */
    @TableField("F_RETURNDATE")
    private Date returnDate;

    /**
     * 档案属性
     */
    @TableField("F_ARCHIVALATTRIBUTES")
    private String archivalAttributes;

    /**
     * 借阅方式
     */
    @TableField("F_BORROWMODE")
    private String borrowMode;

    /**
     * 申请原因
     */
    @TableField("F_APPLYREASON")
    private String applyReason;

    /**
     * 档案编码
     */
    @TableField("F_ARCHIVESID")
    private String archivesId;
}
