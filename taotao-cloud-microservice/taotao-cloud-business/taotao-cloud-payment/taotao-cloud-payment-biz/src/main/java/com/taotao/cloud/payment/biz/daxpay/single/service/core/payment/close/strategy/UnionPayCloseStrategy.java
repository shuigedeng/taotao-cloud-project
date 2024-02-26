package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.close.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayCloseStrategy extends AbsPayCloseStrategy {


    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {

    }
}
