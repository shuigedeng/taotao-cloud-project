package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 文件签阅表
 */
@Data
@TableName("wform_documentsigning")
public class DocumentSigningEntity {
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
     * 文件编码
     */
    @TableField("F_FILLNUM")
    private String fillNum;

    /**
     * 拟稿人
     */
    @TableField("F_DRAFTEDPERSON")
    private String draftedPerson;


    /**
     * 签阅人
     */
    @TableField("F_READER")
    private String reader;

    /**
     * 文件拟办
     */
    @TableField("F_FILLPREPARATION")
    private String fillPreparation;

    /**
     * 签阅时间
     */
    @TableField("F_CHECKDATE")
    private Date checkDate;

    /**
     * 发稿日期
     */
    @TableField("F_PUBLICATIONDATE")
    private Date publicationDate;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 文件内容
     */
    @TableField("F_DOCUMENTCONTENT")
    private String documentContent;

    /**
     * 建议栏
     */
    @TableField("F_ADVICECOLUMN")
    private String adviceColumn;
}
