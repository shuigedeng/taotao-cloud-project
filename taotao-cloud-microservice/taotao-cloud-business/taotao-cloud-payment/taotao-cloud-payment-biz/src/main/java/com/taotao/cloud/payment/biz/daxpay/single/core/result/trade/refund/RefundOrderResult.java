package com.taotao.cloud.payment.biz.daxpay.single.core.result.trade.refund;

import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款订单数据")
public class RefundOrderResult extends MchAppResult {

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 通道支付订单号 */
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    private String title;

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /** 通道退款交易号 */
    @Schema(description = "通道退款交易号")
    private String outRefundNo;

    /**
     * 退款通道
     * @see ChannelEnum
     */
    @Schema(description = "退款通道")
    private String channel;

    /** 订单金额 */
    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private BigDecimal amount;

    /** 退款原因 */
    @Schema(description = "退款原因")
    private String reason;

    /** 退款结束时间 */
    @Schema(description = "退款结束时间")
    private LocalDateTime finishTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @Schema(description = "商户扩展参数,回调时会原样返回, 以最后一次为准")
    private String attach;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;

}
