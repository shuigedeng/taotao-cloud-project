package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 领料明细
 */
@Data
@TableName("wform_materialrequisitionentry")
public class MaterialEntryEntity {
    /**
     * 主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 领料主键
     */
    @TableField("F_LEADEID")
    private String leadeId;

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
     * 需数量
     */
    @TableField("F_MATERIALDEMAND")
    private String materialDemand;

    /**
     * 配数量
     */
    @TableField("F_PROPORTIONING")
    private String proportioning;

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
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 排序码
     */
    @TableField("F_SORTCODE")
    private Long sortCode;
}
