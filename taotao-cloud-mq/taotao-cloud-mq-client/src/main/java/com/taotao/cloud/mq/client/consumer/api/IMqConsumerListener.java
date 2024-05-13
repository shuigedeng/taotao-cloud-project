package com.taotao.cloud.ttcmq.client.consumer.api;


/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqConsumerListener {


    /**
     * 消费
     * @param mqMessage 消息体
     * @param context 上下文
     * @return 结果
     */
    ConsumerStatus consumer(final MqMessage mqMessage,
                            final IMqConsumerListenerContext context);

}
