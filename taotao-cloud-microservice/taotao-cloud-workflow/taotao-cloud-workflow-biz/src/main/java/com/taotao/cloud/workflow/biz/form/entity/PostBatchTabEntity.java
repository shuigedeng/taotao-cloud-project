package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 发文呈批表
 *
 */
@Data
@TableName("wform_postbatchtab")
public class PostBatchTabEntity {
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
     * 文件标题
     */
    @TableField("F_FILETITLE")
    private String fileTitle;

    /**
     * 主办单位
     */
    @TableField("F_DRAFTEDPERSON")
    private String draftedPerson;

    /**
     * 发往单位
     */
    @TableField("F_SENDUNIT")
    private String sendUnit;

    /**
     * 发文编码
     */
    @TableField("F_WRITINGNUM")
    private String writingNum;

    /**
     * 发文日期
     */
    @TableField("F_WRITINGDATE")
    private Date writingDate;

    /**
     * 份数
     */
    @TableField("F_SHARENUM")
    private String shareNum;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;
}
