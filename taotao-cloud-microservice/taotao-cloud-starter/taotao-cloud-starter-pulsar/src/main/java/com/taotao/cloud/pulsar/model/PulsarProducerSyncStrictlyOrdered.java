package com.taotao.cloud.pulsar.model;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;

/**
 * @author hezhangjian
 */
public class PulsarProducerSyncStrictlyOrdered {

    Producer<byte[]> producer;

    public void sendMsg(byte[] msg) {
        while (true) {
            try {
                final MessageId messageId = producer.send(msg);
                LogUtil.info("topic {} send success, msg id is {}", producer.getTopic(), messageId);
                break;
            } catch (Exception e) {
	            LogUtil.error("exception is ", e);
            }
        }
    }

}
