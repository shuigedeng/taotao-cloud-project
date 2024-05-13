package com.taotao.cloud.ttcmq.client.consumer.support.listener;


import com.taotao.cloud.ttcmq.client.consumer.api.IMqConsumerListener;
import com.taotao.cloud.ttcmq.client.consumer.api.IMqConsumerListenerContext;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqListenerService {

    /**
     * 注册
     * @param listener 监听器
     * @since 2024.05
     */
    void register(final IMqConsumerListener listener);

    /**
     * 消费消息
     * @param mqMessage 消息
     * @param context 上下文
     * @return 结果
     * @since 2024.05
     */
    ConsumerStatus consumer(final MqMessage mqMessage,
                            final IMqConsumerListenerContext context);

}
