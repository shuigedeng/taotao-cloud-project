package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.mq.base.*;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * $RabbitMQProducerProvider 发送消息提供者组件
 *
 * @author clz.xu
 */
@Slf4j
public class RabbitMQProducerProvider extends AbstractProducerProvider {

    protected final ConnectionFactory connectionFactory;
    protected AbstractProducer producer;

    public RabbitMQProducerProvider(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        producer = new AbstractProducer();
        producer.setObject(this);
    }


    /**
     * 发送消息
     *
     * @param message 消息对象
     * @return
     */
    @Override
    public <T> AbstractProducer sendMessage(AbstractSendMessage<T> message) {
        return RabbitMQMonitor.hook().run("rabbitmq-produce", () -> {
            RabbitMQSendMessage<T> msg = null;
            if(message instanceof RabbitMQSendMessage){
                msg=(RabbitMQSendMessage<T>)message;
            }else{
                msg= RabbitMQSendMessage.from(message);
            }
            try {
                //从connection缓存池中获取,并放回池
                try(var conn=connectionFactory.createConnection()){
                    //从channel缓存池中获取,并放回池
                    try(var channel = conn.createChannel(false)){
                        bindingQueueWithExchange(channel,msg, connectionFactory);
                        if (Objects.nonNull(channel)) {
                            String msgJson = null;
                            if (msg.getMsg()!=null && msg.getMsg() instanceof String) {
                                msgJson = (String) msg.getMsg();
                            } else {
                                msgJson = JsonUtils.serialize(msg.getMsg());
                            }
                            if (Objects.nonNull(msg.getDelayTimeEnum()) &&
                                    msg.getDelayTimeEnum().getSecond() > 0) {
                                //延迟消息
                                Map<String, Object> headers = new HashMap<>(1);
                                headers.put("x-delay", msg.getDelayTimeEnum().getSecond() * 1000);
                                AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
                                channel.basicPublish(msg.getExchange(), msg.getRoutingKey(), true, props.build(), msgJson.getBytes(RemotingHelper.DEFAULT_CHARSET));
                            } else {
                                channel.basicPublish(msg.getExchange(), msg.getRoutingKey(), true, null, msgJson.getBytes(RemotingHelper.DEFAULT_CHARSET));
                            }

                        }
                        return producer;
                    }
                }
            } catch (Exception ex) {
                LogUtils.error(RabbitMQProducerProvider.class, RabbitMQProperties.Project, "rabbitmq 生产者发送失败", ex);
                throw new MQException(ex);
            }
        });
    }

    @Override
    public void close() {
        //do nothing
//        try {
//
//        } catch (Exception exp) {
//            LogUtils.error(RabbitMQProducerProvider.class, RabbitMQProperties.Project, "生产者资源释放失败", exp);
//        }
    }

    /**
     * 声明交换机
     * 绑定队列
     *
     * @param message 消息对象
     * @throws Exception
     */
    private void bindingQueueWithExchange(Channel channel, RabbitMQSendMessage message, ConnectionFactory connectionFactory) throws IOException {
        val exchangeType = message.getExchangeTypeEnum();
        if (exchangeType==ExchangeTypeEnum.DIRECT) {
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.DIRECT, true, false, null);
        } else if(exchangeType==ExchangeTypeEnum.TOPIC){
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.TOPIC, true, false, null);
        }
        else if(exchangeType==ExchangeTypeEnum.FANOUT){
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.FANOUT, true, false, null);
        }
        else if(exchangeType==ExchangeTypeEnum.DELAYED){
            Map<String, Object> args = new HashMap<>(1);
            args.put("x-delayed-type", "direct");
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.HEADERS, true, false, args);
        }
        Map<String, Object> argsMap = new HashMap<>(2);
        argsMap.put("x-dead-letter-exchange", "dead.letter.exchange");
        argsMap.put("x-dead-letter-routing-key", "dead.letter.routing.key");
        Map<String, Object> args = message.isEnableDeadQueue() ? argsMap : null;
        channel.queueDeclare(message.getQueueName(), true, false, false, args);
        channel.queueBind(message.getQueueName(), message.getExchange(), message.getRoutingKey());
    }

}
