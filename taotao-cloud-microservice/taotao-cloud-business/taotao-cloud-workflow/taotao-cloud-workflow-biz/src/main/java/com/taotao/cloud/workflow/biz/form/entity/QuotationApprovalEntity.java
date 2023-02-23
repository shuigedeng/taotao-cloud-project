package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 报价审批表
 */
@Data
@TableName("wform_quotationapproval")
public class QuotationApprovalEntity {
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
     * 流程等级
     */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /**
     * 流程单据
     */
    @TableField("F_BILLNO")
    private String billNo;


    /**
     * 填报人
     */
    @TableField("F_WRITER")
    private String writer;

    /**
     * 填表日期
     */
    @TableField("F_WRITEDATE")
    private Date writeDate;

    /**
     * 客户名称
     */
    @TableField("F_CUSTOMERNAME")
    private String customerName;

    /**
     * 类型
     */
    @TableField("F_QUOTATIONTYPE")
    private String quotationType;

    /**
     * 合作人名
     */
    @TableField("F_PARTNERNAME")
    private String partnerName;

    /**
     * 模板参考
     */
    @TableField("F_STANDARDFILE")
    private String standardFile;

    /**
     * 情况描述
     */
    @TableField("F_CUSTSITUATION")
    private String custSituation;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;
}
