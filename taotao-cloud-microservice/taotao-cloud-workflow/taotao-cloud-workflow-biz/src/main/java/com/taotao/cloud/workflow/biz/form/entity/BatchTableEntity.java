package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 行文呈批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("wform_batchtable")
public class BatchTableEntity {
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
     * 文件编码
     */
    @TableField("F_FILLNUM")
    private String fillNum;

    /**
     * 发往单位
     */
    @TableField("F_SENDUNIT")
    private String sendUnit;

    /**
     * 打字
     */
    @TableField("F_TYPING")
    private String typing;

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
