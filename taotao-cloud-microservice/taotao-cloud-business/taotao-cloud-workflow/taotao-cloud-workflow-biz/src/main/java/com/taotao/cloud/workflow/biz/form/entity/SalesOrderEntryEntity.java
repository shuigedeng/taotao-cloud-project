package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 销售订单明细
 */
@Data
@TableName("wform_salesorderentry")
public class SalesOrderEntryEntity {
    /**
     * 发货明细主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 订单主键
     */
    @TableField("F_SALESORDERID")
    private String salesOrderId;

    /**
     * 商品名称
     */
    @TableField("F_GOODSNAME")
    private String goodsName;

    /**
     * 规格型号
     */
    @TableField("F_SPECIFICATIONS")
    private String specifications;

    /**
     * 单位
     */
    @TableField("F_UNIT")
    private String unit;

    /**
     * 数量
     */
    @TableField("F_QTY")
    private BigDecimal qty;

    /**
     * 单价
     */
    @TableField("F_PRICE")
    private BigDecimal price;

    /**
     * 金额
     */
    @TableField("F_AMOUNT")
    private BigDecimal amount;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;


    /**
     * 排序码
     */
    @TableField("F_SORTCODE")
    private Long sortCode;
}
