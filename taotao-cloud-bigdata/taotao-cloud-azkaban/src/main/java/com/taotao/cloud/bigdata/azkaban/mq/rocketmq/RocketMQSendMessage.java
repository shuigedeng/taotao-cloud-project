package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;

import com.free.bsf.core.base.Callable;
import com.free.bsf.mq.base.AbstractSendMessage;
import com.free.bsf.mq.base.DelayTimeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname RocketMQSendMessage
 * @Description RocketMQ专属消息体
 * @Date 2021/6/2 14:08
 * @Created by chejiangyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class RocketMQSendMessage<T> extends AbstractSendMessage<T> {
    /**
     *分类标签
     */
    String tag;
    /**
     * 索引keys
     */
    String[] keys;
    /**
     * 延迟时间
     */
    DelayTimeEnum delayTimeEnum;
    /**
     * 分区算法
     */
    Callable.Func1<Integer,Integer> partitionSelector;

    public static <T> RocketMQSendMessage<T> from(AbstractSendMessage<T> message){
        return (RocketMQSendMessage<T>)new RocketMQSendMessage<T>().setQueueName(message.getQueueName()).setMsg(message.getMsg());
    }
}
