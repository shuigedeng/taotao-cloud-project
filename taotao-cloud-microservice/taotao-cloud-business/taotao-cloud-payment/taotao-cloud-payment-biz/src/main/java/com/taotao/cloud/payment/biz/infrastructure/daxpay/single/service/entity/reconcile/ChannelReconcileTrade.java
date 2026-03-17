package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.entity.reconcile;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TradeTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 通道对账交易明细, 通过解析通道对账文件获得,
 * @author xxm
 * @since 2024/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data

@TableName("pay_channel_reconcile_trade")
public class ChannelReconcileTrade extends MchAppRecordEntity {

    /** 关联对账单ID */
    private Long reconcileId;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /** 平台交易号 */
    private String outTradeNo;

    /** 通道交易号 */
    private String tradeNo;

    /** 交易金额 */
    private BigDecimal amount;

    /**
     * 交易状态
     * @see TradeStatusEnum
     */
    private String tradeStatus;

    /** 交易时间 */
    private LocalDateTime tradeTime;
}
