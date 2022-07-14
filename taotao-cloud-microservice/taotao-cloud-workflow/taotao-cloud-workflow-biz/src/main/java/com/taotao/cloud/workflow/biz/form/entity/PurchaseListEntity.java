package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 日常物品采购清单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_purchaselist")
public class PurchaseListEntity {
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
     * 申请人
     */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /**
     * 所在部门
     */
    @TableField("F_DEPARTMENTAL")
    private String departmental;

    /**
     * 供应商名称
     */
    @TableField("F_VENDORNAME")
    private String vendorName;

    /**
     * 采购人员
     */
    @TableField("F_BUYER")
    private String buyer;

    /**
     * 采购日期
     */
    @TableField("F_PURCHASEDATE")
    private Date purchaseDate;

    /**
     * 仓库
     */
    @TableField("F_WAREHOUSE")
    private String warehouse;

    /**
     * 联系方式
     */
    @TableField("F_TELEPHONE")
    private String telephone;

    /**
     * 支付方式
     */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /**
     * 支付总额
     */
    @TableField("F_PAYMENTMONEY")
    private BigDecimal paymentMoney;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 用途原因
     */
    @TableField("F_REASON")
    private String reason;
}
