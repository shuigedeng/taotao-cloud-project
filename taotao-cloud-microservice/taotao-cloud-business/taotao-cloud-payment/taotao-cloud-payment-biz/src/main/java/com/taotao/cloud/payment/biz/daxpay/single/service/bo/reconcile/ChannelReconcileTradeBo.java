package com.taotao.cloud.payment.biz.daxpay.single.service.bo.reconcile;

import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付通道交易对账单
 * @author xxm
 * @since 2024/8/6
 */
@Data
@Accessors(chain = true)
public class ChannelReconcileTradeBo {

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /**
     * 交易状态
     * @see TradeStatusEnum
     */
    private String tradeStatus;

    /** 平台交易号 */
    private String outTradeNo;

    /** 通道交易号 */
    private String tradeNo;

    /** 交易金额 */
    private BigDecimal amount;

    /** 交易时间 */
    private LocalDateTime tradeTime;
}
