package com.taotao.cloud.payment.biz.daxpay.single.service.result.record.flow;

import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeFlowTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

import java.math.BigDecimal;

/**
 * 资金流水记录
 * @author xxm
 * @since 2024/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "资金流水记录")
public class TradeFlowRecordResult extends MchAppResult {

    /** 订单标题 */
    private String title;

    /** 金额 */
    private BigDecimal amount;

    /**
     * 业务类型
     * @see TradeFlowTypeEnum
     */
    private String type;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 平台交易号 */
    private String tradeNo;

    /** 商户交易号 */
    private String bizTradeNo;

    /** 通道交易号 */
    private String outTradeNo;
}
