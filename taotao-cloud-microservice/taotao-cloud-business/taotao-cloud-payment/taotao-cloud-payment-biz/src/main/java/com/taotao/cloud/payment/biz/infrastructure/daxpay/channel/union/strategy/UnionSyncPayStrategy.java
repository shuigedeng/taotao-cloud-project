package com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.strategy;

import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.PaySyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncPayOrderStrategy;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.sdk.api.UnionPayKit;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.service.config.UnionPayConfigService;
import com.taotao.cloud.payment.biz.infrastructure.daxpay.channel.union.service.sync.UnionPaySyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付同步接口
 * @author xxm
 * @since 2024/3/7
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionSyncPayStrategy extends AbsSyncPayOrderStrategy {

    private final UnionPayConfigService unionPayConfigService;

    private final UnionPaySyncService unionPaySyncService;

    @Override
    public PaySyncResultBo doSync() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit();
        return unionPaySyncService.sync(this.getOrder(),unionPayKit);
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
