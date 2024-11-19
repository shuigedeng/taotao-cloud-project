package com.taotao.cloud.payment.biz.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.channel.union.sdk.api.UnionPayKit;
import com.taotao.cloud.payment.biz.daxpay.channel.union.service.config.UnionPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.union.service.sync.UnionRefundSyncService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.RefundSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付退款同步
 * @author xxm
 * @since 2024/3/11
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionSyncRefundStrategy extends AbsSyncRefundOrderStrategy {
    private final UnionPayConfigService unionPayConfigService;
    private final UnionRefundSyncService unionSyncRefundService;

    @Override
    public RefundSyncResultBo doSync() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        return unionSyncRefundService.sync(this.getRefundOrder(),unionPayKit);
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
