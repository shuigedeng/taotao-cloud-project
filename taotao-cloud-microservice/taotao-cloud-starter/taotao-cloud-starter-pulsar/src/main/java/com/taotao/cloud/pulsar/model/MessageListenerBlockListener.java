package com.taotao.cloud.pulsar.model;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

import java.util.concurrent.Semaphore;

public class MessageListenerBlockListener<T> implements MessageListener<T> {

    /**
     * Semaphore保证最多同时处理500条消息
     */
    private final Semaphore semaphore = new Semaphore(500);

    @Override
    public void received(Consumer<T> consumer, Message<T> msg) {
        try {
            semaphore.acquire();
            asyncPayload(msg.getData(), e -> {
                semaphore.release();
                if (e == null) {
                    consumer.acknowledgeAsync(msg);
                } else {
	                LogUtil.error("exception is ", e);
                    consumer.negativeAcknowledge(msg);
                }
            });
        } catch (Exception e) {
            semaphore.release();
            // 业务方法可能会抛出异常
            consumer.negativeAcknowledge(msg);
        }
    }

    /**
     * 模拟异步执行的业务方法
     * @param msg 消息体
     * @param sendCallback 异步函数的callback
     */
    private void asyncPayload(byte[] msg, SendCallback sendCallback) {
        if (System.currentTimeMillis() % 2 == 0) {
            sendCallback.callback(null);
        } else {
            sendCallback.callback(new Exception("exception"));
        }
    }

}
