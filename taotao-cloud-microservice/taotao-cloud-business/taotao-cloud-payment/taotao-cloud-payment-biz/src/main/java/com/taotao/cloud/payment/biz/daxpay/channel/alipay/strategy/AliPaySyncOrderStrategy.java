package com.taotao.cloud.payment.biz.daxpay.channel.alipay.strategy;

import lombok.RequiredArgsConstructor;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.service.sync.AliPaySyncService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.PaySyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncPayOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPaySyncOrderStrategy extends AbsSyncPayOrderStrategy {

    private final AliPaySyncService alipaySyncService;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResultBo doSync() {
        return alipaySyncService.syncPayStatus(this.getOrder());
    }
}
