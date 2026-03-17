package com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.strategy;

import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.RefundSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.sdk.api.UnionPayKit;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.service.config.UnionPayConfigService;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.service.sync.UnionRefundSyncService;
import lombok.RequiredArgsConstructor;
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
