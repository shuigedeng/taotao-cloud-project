package com.taotao.cloud.payment.biz.daxpay.channel.wechat.strategy;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.sync.transfer.WechatTransferSyncV3Service;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.TransferSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncTransferOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatSyncTransferStrategy extends AbsSyncTransferOrderStrategy {

    private final WechatTransferSyncV3Service wechatTransferSyncV3Service;

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     */
    @Override
    public TransferSyncResultBo doSync() {
        var wechatPayConfig = wechatPayConfigService.getAndCheckConfig();
        return wechatTransferSyncV3Service.sync(this.getTransferOrder(), wechatPayConfig);
    }

}
