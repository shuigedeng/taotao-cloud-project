package com.taotao.cloud.payment.biz.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.channel.union.sdk.api.UnionPayKit;
import com.taotao.cloud.payment.biz.daxpay.channel.union.service.config.UnionPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.union.service.refund.UnionRefundService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.trade.RefundResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsRefundStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionRefundStrategy extends AbsRefundStrategy {

    private final UnionRefundService unionRefundService;

    private final UnionPayConfigService unionPayConfigService;

    /**
     * 策略标识
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }

    /**
     * 退款操作
     */
    @Override
    public RefundResultBo doRefundHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        return unionRefundService.refund(this.getRefundOrder(), unionPayKit);
    }
}
