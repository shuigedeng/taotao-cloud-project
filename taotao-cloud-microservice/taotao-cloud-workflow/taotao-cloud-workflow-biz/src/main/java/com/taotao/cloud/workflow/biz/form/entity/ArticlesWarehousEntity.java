package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 用品入库申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("wform_articleswarehous")
public class ArticlesWarehousEntity {
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
     * 用品库存
     */
    @TableField("F_ARTICLES")
    private String articles;

    /**
     * 用品分类
     */
    @TableField("F_CLASSIFICATION")
    private String classification;

    /**
     * 用品编码
     */
    @TableField("F_ARTICLESID")
    private String articlesId;

    /**
     * 单位
     */
    @TableField("F_COMPANY")
    private String company;

    /**
     * 数量
     */
    @TableField("F_ESTIMATEPEOPLE")
    private String estimatePeople;

    /**
     * 申请原因
     */
    @TableField("F_APPLYREASONS")
    private String applyReasons;
}
