package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 合同开票流程
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("wform_conbilling")
public class ConBillingEntity {
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
     * 开票人
     */
    @TableField("F_DRAWER")
    private String drawer;

    /**
     * 开票日期
     */
    @TableField("F_BILLDATE")
    private Date billDate;

    /**
     * 公司名称
     */
    @TableField("F_COMPANYNAME")
    private String companyName;

    /**
     * 关联名称
     */
    @TableField("F_CONNAME")
    private String conName;

    /**
     * 开户银行
     */
    @TableField("F_BANK")
    private String bank;

    /**
     * 开户账号
     */
    @TableField("F_AMOUNT")
    private String amount;

    /**
     * 开票金额
     */
    @TableField("F_BILLAMOUNT")
    private BigDecimal billAmount;

    /**
     * 税号
     */
    @TableField("F_TAXID")
    private String taxId;

    /**
     * 发票号
     */
    @TableField("F_INVOICEID")
    private String invoiceId;

    /**
     * 发票地址
     */
    @TableField("F_INVOADDRESS")
    private String invoAddress;

    /**
     * 付款金额
     */
    @TableField("F_PAYAMOUNT")
    private BigDecimal payAmount;

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
