package com.taotao.cloud.payment.biz.bootx.configuration;

import com.taotao.cloud.payment.biz.bootx.code.PaymentEventCode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * @author xxm
 * @date 2021/6/25
 */
@Configuration
public class PaymentRabbitMqConfiguration {

    /** 支付完成队列 */
    @Bean
    public Queue payCompleted() {
        return new Queue(PaymentEventCode.PAY_COMPLETE);
    }
    /** 交换机 */
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PaymentEventCode.EXCHANGE_PAYMENT);
    }
    /** 绑定支付完成 */
    @Bean
    public Binding bindPayCompleted() {
        return BindingBuilder.bind(payCompleted())
                .to(paymentExchange())
                .with(PaymentEventCode.PAY_COMPLETE);
    }
}
