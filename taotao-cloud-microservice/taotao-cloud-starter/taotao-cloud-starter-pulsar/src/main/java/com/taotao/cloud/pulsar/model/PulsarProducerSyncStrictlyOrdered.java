package com.taotao.cloud.pulsar.model;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;

public class PulsarProducerSyncStrictlyOrdered {

    Producer<byte[]> producer;

    public void sendMsg(byte[] msg) {
        while (true) {
            try {
                final MessageId messageId = producer.send(msg);
                LogUtils.info("topic {} send success, msg id is {}", producer.getTopic(), messageId);
                break;
            } catch (Exception e) {
	            LogUtils.error("exception is ", e);
            }
        }
    }

}
