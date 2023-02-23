package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 发货申请单
 *
 */
@Data
@TableName("wform_applydelivergoods")
public class ApplyDeliverGoodsEntity {
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
     * 货品所属
     */
    @TableField("F_GOODSBELONGED")
    private String goodsBelonged;

    /**
     * 发货日期
     */
    @TableField("F_INVOICEDATE")
    private Date invoiceDate;

    /**
     * 货运公司
     */
    @TableField("F_FREIGHTCOMPANY")
    private String freightCompany;

    /**
     * 发货类型
     */
    @TableField("F_DELIVERYTYPE")
    private String deliveryType;

    /**
     * 货运单号
     */
    @TableField("F_RRANSPORTNUM")
    private String rransportNum;

    /**
     * 货运费
     */
    @TableField("F_FREIGHTCHARGES")
    private BigDecimal freightCharges;

    /**
     * 保险金额
     */
    @TableField("F_CARGOINSURANCE")
    private BigDecimal cargoInsurance;

    /**
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 发货金额
     */
    @TableField("F_INVOICEVALUE")
    private BigDecimal invoiceValue;
}
