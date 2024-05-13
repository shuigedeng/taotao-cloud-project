package com.taotao.cloud.mq.client.consumer.support.listener;


/**
 * @author shuigedeng
 * @since 2024.05
 */
@NotThreadSafe
public class MqListenerService implements IMqListenerService {

    private static final Log log = LogFactory.getLog(MqListenerService.class);

    private IMqConsumerListener mqConsumerListener;

    @Override
    public void register(IMqConsumerListener listener) {
        this.mqConsumerListener = listener;
    }

    @Override
    public ConsumerStatus consumer(MqMessage mqMessage, IMqConsumerListenerContext context) {
        if(mqConsumerListener == null) {
            log.warn("当前监听类为空，直接忽略处理。message: {}", JSON.toJSON(mqMessage));
            return ConsumerStatus.SUCCESS;
        } else {
            return mqConsumerListener.consumer(mqMessage, context);
        }
    }
}
