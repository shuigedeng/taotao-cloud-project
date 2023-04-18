package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;

import com.free.bsf.core.util.PropertyUtils;
import com.free.bsf.mq.base.AbstractSubscribeRunable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @Classname RocketMQSubscribeRunable
 * @Description
 * @Date 2021/6/2 14:33
 * @Created by chejiangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RocketMQSubscribeRunable<T> extends AbstractSubscribeRunable<T> {
    String consumerGroup;
    MessageModel mode=MessageModel.CLUSTERING;
    String[] filterTags;

    public static <T> RocketMQSubscribeRunable<T> from(AbstractSubscribeRunable<T> message){
        return (RocketMQSubscribeRunable<T>)new RocketMQSubscribeRunable<T>()
                .setMode(MessageModel.CLUSTERING)
                .setConsumerGroup(PropertyUtils.getPropertyCache("spring.application.name",""))
                .setQueueName(message.getQueueName())
                .setRunnable(message.getRunnable())
                .setType(message.getType());
    }
}
