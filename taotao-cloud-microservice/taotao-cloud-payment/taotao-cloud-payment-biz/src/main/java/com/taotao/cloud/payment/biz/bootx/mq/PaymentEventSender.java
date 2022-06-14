package com.taotao.cloud.payment.biz.bootx.mq;

import com.taotao.cloud.payment.biz.bootx.code.PaymentEventCode;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 支付中心消息发送器
 * @author xxm
 * @date 2021/4/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventSender {

    private final RabbitTemplate rabbitTemplate;
    /**
     * 支付完成 事件发布
     */
    public void sendPaymentCompleted(PayResult event) {
        rabbitTemplate.convertAndSend(
                PaymentEventCode.EXCHANGE_PAYMENT,
                PaymentEventCode.PAY_COMPLETE,
                event
        );
    }
}
