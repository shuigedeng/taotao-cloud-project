package com.taotao.cloud.bigdata.azkaban.mq.base;

import com.free.bsf.mq.rocketmq.RocketMQSubscribeRunable;
import lombok.val;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 14:49
 * 消费者提供者: 提供不同类型的消息队列的消费者
 **/
public class AbstractConsumerProvider extends AbstractConsumer {
    @Deprecated
    public  <T> AbstractConsumer subscribe(String consumergroup, String topic, String[] filterTags, SubscribeRunable<T> runnable, Class<T> type)
    {return subscribe( consumergroup,MessageModel.CLUSTERING,topic,  filterTags, runnable, type);}
    @Deprecated
    public  <T> AbstractConsumer subscribe(String consumergroup,MessageModel mode,  String topic, String[] filterTags, SubscribeRunable<T> runnable, Class<T> type)
    {
        val run= new RocketMQSubscribeRunable<T>();
        run.setConsumerGroup(consumergroup)
                .setFilterTags(filterTags).setMode(mode)
                .setQueueName(topic).setRunnable(runnable).setType(type);
        return subscribe(run);
    }

    public  <T> AbstractConsumer subscribe(AbstractSubscribeRunable<T> subscribeRunable){
        return null;
    }
}

