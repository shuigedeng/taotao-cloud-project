package com.taotao.cloud.payment.biz.daxpay.single.service.bo.reconcile;

import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeTypeEnum;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 平台通用交易对象对象，用于与网关进行对账
 * @author xxm
 * @since 2024/3/1
 */
@Data
@Accessors(chain = true)
public class PlatformReconcileTradeBo {

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /** 金额 */
    private BigDecimal amount;

    /**
     * 交易状态
     * @see TradeStatusEnum
     */
    private String tradeStatus;

    /** 平台交易号 */
    private String tradeNo;

    /** 商户交易号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;

    /** 完成时间 */
    private LocalDateTime tradeTime;
}
