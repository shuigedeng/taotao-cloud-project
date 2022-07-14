package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 销售订单
 *
 */
@Data
@TableName("wform_salesorder")
public class SalesOrderEntity {
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
     * 业务人员
     */
    @TableField("F_SALESMAN")
    private String salesman;

    /**
     * 客户名称
     */
    @TableField("F_CUSTOMERNAME")
    private String customerName;

    /**
     * 联系人
     */
    @TableField("F_CONTACTS")
    private String contacts;

    /**
     * 联系电话
     */
    @TableField("F_CONTACTPHONE")
    private String contactPhone;

    /**
     * 客户地址
     */
    @TableField("F_CUSTOMERADDRES")
    private String customerAddres;

    /**
     * 发票编码
     */
    @TableField("F_TICKETNUM")
    private String ticketNum;

    /**
     * 开票日期
     */
    @TableField("F_TICKETDATE")
    private Date ticketDate;

    /**
     * 发票类型
     */
    @TableField("F_INVOICETYPE")
    private String invoiceType;

    /**
     * 付款方式
     */
    @TableField("F_PAYMENTMETHOD")
    private String paymentMethod;

    /**
     * 付款金额
     */
    @TableField("F_PAYMENTMONEY")
    private BigDecimal paymentMoney;

    /**
     * 销售日期
     */
    @TableField("F_SALESDATE")
    private Date salesDate;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;
}
