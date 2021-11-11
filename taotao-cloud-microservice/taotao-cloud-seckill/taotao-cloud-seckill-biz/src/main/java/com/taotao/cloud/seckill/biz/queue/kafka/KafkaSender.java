package com.taotao.cloud.seckill.biz.queue.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 * 生产者
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 发送消息到kafka
     */
    public void sendChannelMess(String channel, String message){
        kafkaTemplate.send(channel,message);
    }
}
