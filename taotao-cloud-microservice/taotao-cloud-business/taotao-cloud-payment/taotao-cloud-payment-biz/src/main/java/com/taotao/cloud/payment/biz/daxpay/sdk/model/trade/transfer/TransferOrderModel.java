package com.taotao.cloud.payment.biz.daxpay.sdk.model.trade.transfer;

import cn.daxpay.single.sdk.code.ChannelEnum;
import cn.daxpay.single.sdk.code.TransferPayeeTypeEnum;
import cn.daxpay.single.sdk.code.TransferStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 转账订单
 * @author xxm
 * @since 2024/6/20
 */
@Data
public class TransferOrderModel{

    /** 商户转账号 */
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @Schema(description = "转账号")
    private String transferNo;

    /** 通道转账号 */
    @Schema(description = "通道转账号")
    private String outTransferNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 转账金额 */
    @Schema(description = "转账金额")
    private BigDecimal amount;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 转账原因/备注 */
    @Schema(description = "转账原因/备注")
    private String reason;

    /**
     * 收款人类型
     * @see TransferPayeeTypeEnum
     */
    @Schema(description = "收款人类型")
    private String payeeType;

    /** 收款人账号 */
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /** 收款人姓名 */
    @Schema(description = "收款人姓名")
    private String payeeName;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /** 完成时间 */
    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @Schema(description = "商户扩展参数")
    private String attach;

    /**
     * 错误原因
     */
    @Schema(description = "错误原因")
    private String errorMsg;
}