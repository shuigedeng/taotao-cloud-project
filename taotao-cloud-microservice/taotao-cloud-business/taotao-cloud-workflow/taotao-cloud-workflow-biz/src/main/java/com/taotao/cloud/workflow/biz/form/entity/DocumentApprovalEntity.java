package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 文件签批意见表
 */
@Data
@TableName("wform_documentapproval")
public class DocumentApprovalEntity {
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
     * 文件名称
     */
    @TableField("F_FILENAME")
    private String fileName;

    /**
     * 拟稿人
     */
    @TableField("F_DRAFTEDPERSON")
    private String draftedPerson;

    /**
     * 发文单位
     */
    @TableField("F_SERVICEUNIT")
    private String serviceUnit;

    /**
     * 文件拟办
     */
    @TableField("F_FILLPREPARATION")
    private String fillPreparation;

    /**
     * 文件编码
     */
    @TableField("F_FILLNUM")
    private String fillNum;

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
     * 修改意见
     */
    @TableField("F_MODIFYOPINION")
    private String modifyOpinion;
}
