package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.mq.base.AbstractSendMessage;
import com.free.bsf.mq.base.DelayTimeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * $RabbitMessage 消息对象
 *
 * @author clz.xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RabbitMQSendMessage<T> extends AbstractSendMessage<T> {
    /*	消息的主题 */
    private String exchange;

    /*	消息的路由规则	*/
    private String routingKey = "";

    /*	延迟消息的参数 单位 S	*/
    private DelayTimeEnum delayTimeEnum;

    /* 是否需要入死性队列  */
    private boolean enableDeadQueue;

    /* 消息交换机类型 */
    private ExchangeTypeEnum exchangeTypeEnum;

    public static <T> RabbitMQSendMessage<T> from(AbstractSendMessage<T> message){
       return (RabbitMQSendMessage<T>)new RabbitMQSendMessage<T>()
               .setExchange(message.getQueueName()+"_exchange")
               .setRoutingKey("")
               .setQueueName(message.getQueueName())
               .setMsg(message.getMsg());
    }
}
