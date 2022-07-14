package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 领用办公用品申请表
 */
@Data
@TableName("wform_officesupplies")
public class OfficeSuppliesEntity {
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
     * 申请部门
     */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /**
     * 所属部门
     */
    @TableField("F_DEPARTMENT")
    private String department;

    /**
     * 申请时间
     */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /**
     * 领用仓库
     */
    @TableField("F_USESTOCK")
    private String useStock;

    /**
     * 用品分类
     */
    @TableField("F_CLASSIFICATION")
    private String classification;

    /**
     * 用品名称
     */
    @TableField("F_ARTICLESNAME")
    private String articlesName;

    /**
     * 用品数量
     */
    @TableField("F_ARTICLESNUM")
    private String articlesNum;

    /**
     * 用品编码
     */
    @TableField("F_ARTICLESID")
    private String articlesId;

    /**
     * 申请原因
     */
    @TableField("F_APPLYREASONS")
    private String applyReasons;
}
