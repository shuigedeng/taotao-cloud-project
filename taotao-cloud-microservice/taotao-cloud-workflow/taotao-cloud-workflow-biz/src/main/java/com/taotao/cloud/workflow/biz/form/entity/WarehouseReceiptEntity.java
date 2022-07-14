package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 入库申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_warehousereceipt")
public class WarehouseReceiptEntity {
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
     * 供应商名称
     */
    @TableField("F_SUPPLIERNAME")
    private String supplierName;

    /**
     * 联系电话
     */
    @TableField("F_CONTACTPHONE")
    private String contactPhone;

    /**
     * 入库类别
     */
    @TableField("F_WAREHOUSCATEGORY")
    private String warehousCategory;

    /**
     * 仓库
     */
    @TableField("F_WAREHOUSE")
    private String warehouse;

    /**
     * 入库人
     */
    @TableField("F_WAREHOUSESPEOPLE")
    private String warehousesPeople;

    /**
     * 送货单号
     */
    @TableField("F_DELIVERYNO")
    private String deliveryNo;

    /**
     * 入库单号
     */
    @TableField("F_WAREHOUSENO")
    private String warehouseNo;

    /**
     * 入库日期
     */
    @TableField("F_WAREHOUSDATE")
    private Date warehousDate;
}
