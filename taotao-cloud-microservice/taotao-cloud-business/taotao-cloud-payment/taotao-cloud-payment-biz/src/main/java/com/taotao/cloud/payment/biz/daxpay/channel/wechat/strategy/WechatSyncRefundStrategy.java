package com.taotao.cloud.payment.biz.daxpay.channel.wechat.strategy;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.code.WechatPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.config.WechatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.sync.refund.WechatRefundSyncV2Service;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.sync.refund.WechatRefundSyncV3Service;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.sync.RefundSyncResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatSyncRefundStrategy extends AbsSyncRefundOrderStrategy {

    private final WechatRefundSyncV2Service wechatRefundSyncV2Service;

    private final WechatRefundSyncV3Service wechatRefundSyncV3Service;

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
    public RefundSyncResultBo doSync() {
        var wechatPayConfig = wechatPayConfigService.getAndCheckConfig();
        if (Objects.equals(wechatPayConfig.getApiVersion(), WechatPayCode.API_V2)){
            return wechatRefundSyncV2Service.sync(this.getRefundOrder(), wechatPayConfig);
        } else {
            return wechatRefundSyncV3Service.sync(this.getRefundOrder(), wechatPayConfig);
        }
    }

}
