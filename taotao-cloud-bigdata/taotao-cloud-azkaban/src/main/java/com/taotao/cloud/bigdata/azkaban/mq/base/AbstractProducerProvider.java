package com.taotao.cloud.bigdata.azkaban.mq.base;


import com.taotao.cloud.bigdata.azkaban.mq.rocketmq.RocketMQSendMessage;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 14:49 生产者提供者:提供不同类型队列的生产者
 **/
public class AbstractProducerProvider extends AbstractProducer {

	@Deprecated
	public <T> AbstractProducer sendMessage(String topic, T msg) {
		return this.sendMessage(topic,msg,null);
	}

	@Deprecated
	public <T> AbstractProducer sendMessage(String topic, T msg, String tag,DelayTimeEnum delayTimeEnum, Callable.Func1<Integer,Integer> partitionSelector) {
		return this.sendMessage(topic,msg,tag,null,delayTimeEnum,partitionSelector);
	}
	@Deprecated
	public <T> AbstractProducer sendMessage(String topic, T msg, String tag,String[] keys,DelayTimeEnum delayTimeEnum, Callable.Func1<Integer,Integer> partitionSelector) {
		val m = new RocketMQSendMessage<T>();
		m.setTag(tag).setKeys(keys)
		.setDelayTimeEnum(delayTimeEnum).setPartitionSelector(partitionSelector)
		.setQueueName(topic).setMsg(msg);
		return sendMessage(m);
	}
	@Deprecated
	public <T> AbstractProducer sendMessage(String topic, T msg,String tag) {
		return this.sendMessage(topic,msg,tag,DelayTimeEnum.None,null);
	}

	/**
	 *  rabbitmq 发送消息
	 * @param message
	 * @param <T>
	 * @return
	 */
	public <T> AbstractProducer sendMessage(AbstractSendMessage<T> message) {
		return this.sendMessage(message);
	}

}
