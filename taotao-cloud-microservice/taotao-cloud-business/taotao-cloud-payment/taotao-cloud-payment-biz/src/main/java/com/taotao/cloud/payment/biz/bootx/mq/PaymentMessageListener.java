package com.taotao.cloud.payment.biz.bootx.mq;

import com.taotao.cloud.payment.biz.bootx.code.PaymentEventCode;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收
 * @author xxm
 * @date 2021/4/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageListener {

    /**
     * 支付成功
     */
    @RabbitListener(queues = PaymentEventCode.PAY_COMPLETE)
    public void payCancel(PayResult payResult) {
        log.info("支付完成事件:{}",payResult);
    }
}
