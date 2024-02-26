package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayRepairStrategy extends AbsPayRepairStrategy {


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseLocalHandler() {

    }
}
