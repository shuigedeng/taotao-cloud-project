package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;


import com.free.bsf.core.util.LogUtils;
import com.taobao.api.internal.util.StringUtils;
import com.taotao.cloud.bigdata.azkaban.mq.base.*;

import java.util.Arrays;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 13:01
 * rocketmq生产者提供使用提供类
 **/
public class RocketMQProducerProvider extends AbstractProducerProvider {
    protected volatile DefaultMQProducer object;
    protected AbstractProducer producer;
    protected Object _lock = new Object();

    @Autowired
    RocketMQProperties rocketMQProperties;

    public DefaultMQProducer getProducer()
    {
        if(object == null) {
            synchronized(_lock) {
                if(object == null) {
                    DefaultMQProducer instance=null;
                    try {
                        if(rocketMQProperties.getAliyunEnabled())
                        {
                            instance = new DefaultMQProducer("free-mq",rocketMQProperties.getAclRPCHook());
                        }else {
                            instance = new DefaultMQProducer("free-mq");
                        }
                        instance.setNamesrvAddr(rocketMQProperties.getNamesrvaddr());
                        if(rocketMQProperties.getIsUseVIPChannel()!=null) {
                            instance.setVipChannelEnabled(rocketMQProperties.getIsUseVIPChannel());
                        }
                        instance.start();
                        producer = new AbstractProducer();
                        producer.setObject(instance);
                        this.object=instance;
                        LogUtils.info(RocketMQProducerProvider.class,RocketMQProperties.Project,"生产者初始化成功");
                    }
                    catch (Exception exp)
                    {
                        LogUtils.error(RocketMQProducerProvider.class,RocketMQProperties.Project,"生产者初始化失败",exp);
                        if(instance!=null)
                        {
                            try{instance.shutdown();}catch (Exception e){}
                            instance=null;
                        }
                        this.close();
                        throw new MQException(exp);
                    }
                }
            }
        }
        return object;
    }

	@Override
	public <T> AbstractProducer sendMessage(AbstractSendMessage<T> message) {
        return RocketMQMonitor.hook().run("produce", ()->{
            RocketMQSendMessage<T> msg =null;
            if(message instanceof RocketMQSendMessage){
                msg=(RocketMQSendMessage<T>) message;
            }else{
                msg=RocketMQSendMessage.from(message);
            }
            val msg2= msg;
            try {
                String msgJson = null;
                if (msg.getMsg()!=null&&msg.getMsg() instanceof String) {
                    msgJson = (String) msg.getMsg();
                } else {
                    msgJson = JsonUtils.serialize(msg.getMsg());
                }
                Message message2 = new Message(msg.getQueueName(), StringUtils.isEmpty(msg.getTag()) ? "" : msg.getTag(), msgJson.getBytes(RemotingHelper.DEFAULT_CHARSET));
                if(msg.getKeys()!=null){
                    message2.setKeys(Arrays.asList(msg.getKeys()));
                }
                if(msg.getDelayTimeEnum()!= DelayTimeEnum.None) {
                    message2.setDelayTimeLevel(msg.getDelayTimeEnum().getCode());
                }
                if(msg.getPartitionSelector()!=null) {
                    getProducer().send(message2, (queues, m, arg) -> {
                        return queues.get(msg2.getPartitionSelector().invoke(queues.size()) % queues.size());
                    }, null);
                }else{
                    getProducer().send(message2);
                }
                return producer;
            } catch (Exception exp) {
                LogUtils.error(RocketMQProducerProvider.class,RocketMQProperties.Project,"生产者消息发送失败",exp);
                throw new MQException(exp);
            }
        });
	}

	@Override
    public void close() {
        try {
            if (object != null) {
                object.shutdown();
                object = null;
                LogUtils.info(RocketMQProducerProvider.class,RocketMQProperties.Project,"生产者资源释放成功");
            }
            super.close();
        }
        catch (Exception exp)
        {
            LogUtils.error(RocketMQProducerProvider.class,RocketMQProperties.Project,"生产者资源释放失败",exp);
        }
    }
}
