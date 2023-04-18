package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;

import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.mq.base.*;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 13:01
 * rocketmq的消费者封装使用提供类
 **/
public class RocketMQConsumerProvider extends AbstractConsumerProvider{

    @Autowired
    RocketMQProperties rocketMQProperties;

    @Override
    public  <T> AbstractConsumer subscribe(AbstractSubscribeRunable<T> subscribeRunable) {
         RocketMQSubscribeRunable subscribe =null;
    	 if(subscribeRunable instanceof RocketMQSubscribeRunable){
             subscribe=(RocketMQSubscribeRunable) subscribeRunable;
         }else{
             subscribe=RocketMQSubscribeRunable.from(subscribeRunable);
         }
    	 DefaultMQPushConsumer consumer =null;
         try {
             if(rocketMQProperties.getAliyunEnabled())
             {
                 consumer = new DefaultMQPushConsumer(subscribe.getConsumerGroup(),rocketMQProperties.getAclRPCHook(),new AllocateMessageQueueAveragely());
             }else {
                 consumer = new DefaultMQPushConsumer(subscribe.getConsumerGroup());
             }
             // Specify name server addresses.
             consumer.setNamesrvAddr(rocketMQProperties.getNamesrvaddr());
             // set a random instance name,in order to avoid generate same client id in docker environment
             consumer.setInstanceName(UUID.randomUUID().toString());
             
             if(rocketMQProperties.getIsUseVIPChannel()!=null) {
             	consumer.setVipChannelEnabled(rocketMQProperties.getIsUseVIPChannel());
             }
             if(rocketMQProperties.getConsumeThreadMin()!=null) {
            	 consumer.setConsumeThreadMin(rocketMQProperties.getConsumeThreadMin()); 
             }
             
             if(rocketMQProperties.getConsumeThreadMax()!=null)
             { 
            	 // Specify the thread max count of consumer,  default the min count equals max count
             	if(rocketMQProperties.getConsumeThreadMax()>rocketMQProperties.getConsumeThreadMin()) {
             		consumer.setConsumeThreadMax(rocketMQProperties.getConsumeThreadMax()); 
             	}
             }
             if(subscribe.getMode()!=null) {
            	 consumer.setMessageModel(subscribe.getMode());
             }
             
             String filtertag = null;
             // Subscribe one more more topics to consume.
             if(subscribe.getFilterTags() ==null || subscribe.getFilterTags().length==0) {
                 filtertag="*";
             }
             else
             {
                 filtertag= StringUtils.join( subscribe.getFilterTags(),"||");
             }
             consumer.subscribe(subscribe.getQueueName(), filtertag);
             val subscribe2=subscribe;
             // Register callback to execute on arrival of messages fetched from brokers.
             consumer.registerMessageListener(new MessageListenerConcurrently() {
                 @Override
                 public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                     MessageExt messageExt = msgs.get(0);
                     try {
                     	String id=messageExt.getMsgId();
                         byte[] body = messageExt.getBody();
                         String tags = messageExt.getTags();
                         String topic = messageExt.getTopic();
                         String keys = messageExt.getKeys();
                         String msg = new String(body, RemotingHelper.DEFAULT_CHARSET);
                         RocketMQMonitor.hook().run("consume", () -> {
                             if(subscribe2.getType() == String.class)
                             {
                                 subscribe2.getRunnable().run(new Message<T>(id,topic,tags, (T)msg));
                             }
                             else
                             {
                                 subscribe2.getRunnable().run(new Message<T>(id,topic,tags, JsonUtils.deserialize(msg,subscribe2.getType())));
                             }
                             return null;
                         });
                        
                        
                     } catch (Exception e) {
                         LogUtils.error(RocketMQConsumerProvider.class,RocketMQProperties.Project,String.format("rocketmq 消费者%s,消费异常",subscribe2.getConsumerGroup()),e);
                         //处理出现异常，获取重试次数.达到某个次数的时候可以记录日志，做补偿处理
                         int reconsumeTimes = messageExt.getReconsumeTimes();
                         if (reconsumeTimes < rocketMQProperties.getReconsumeTimes()) {
                        	 context.setDelayLevelWhenNextConsume(3);//delay level 
                             return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                         }
                     }

                     return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                 }
             });
             consumer.start();
             AbstractConsumer abstractConsumer = new AbstractConsumer();
             abstractConsumer.setObject(consumer);
             LogUtils.info(RocketMQConsumerProvider.class,RocketMQProperties.Project,String.format("rocketmq 消费者%s,队列%s 启动成功",subscribe2.getConsumerGroup(),subscribe2.getQueueName()));
             return abstractConsumer;
         }
         catch (Exception exp)
         {
             LogUtils.error(RocketMQConsumerProvider.class,RocketMQProperties.Project,String.format("rocketmq 消费者%s,队列%s 启动失败",subscribe.getConsumerGroup(),subscribe.getQueueName()),exp);
             if(consumer!=null)
             {
                 try {
                     consumer.shutdown();
                 }catch (Exception e){}
                 consumer = null;
             }
             throw new MQException(exp);
         }
    }

}
