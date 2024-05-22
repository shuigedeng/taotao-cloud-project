package com.taotao.cloud.mq.client.producer.support.broker;

import com.taotao.cloud.mq.client.producer.dto.SendBatchResult;
import com.taotao.cloud.mq.client.producer.dto.SendResult;
import com.taotao.cloud.mq.common.api.Destroyable;
import com.taotao.cloud.mq.common.dto.req.MqCommonReq;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import io.netty.channel.Channel;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IProducerBrokerService extends Destroyable {

	/**
	 * 初始化列表
	 *
	 * @param config 配置
	 * @since 2024.05
	 */
	void initChannelFutureList(final ProducerBrokerConfig config);

	/**
	 * 注册到服务端
	 *
	 * @since 2024.05
	 */
	void registerToBroker();

	/**
	 * 调用服务端
	 *
	 * @param channel   调用通道
	 * @param commonReq 通用请求
	 * @param respClass 类
	 * @param <T>       泛型
	 * @param <R>       结果
	 * @return 结果
	 * @since 2024.05
	 */
	<T extends MqCommonReq, R extends MqCommonResp> R callServer(Channel channel,
		T commonReq,
		Class<R> respClass);

	/**
	 * 获取请求通道
	 *
	 * @param key 标识
	 * @return 结果
	 * @since 2024.05
	 */
	Channel getChannel(String key);

	/**
	 * 同步发送消息
	 *
	 * @param mqMessage 消息类型
	 * @return 结果
	 */
	SendResult send(final MqMessage mqMessage);

	/**
	 * 单向发送消息
	 *
	 * @param mqMessage 消息类型
	 * @return 结果
	 */
	SendResult sendOneWay(final MqMessage mqMessage);


	/**
	 * 同步发送消息-批量 1. 必须具有相同的 shardingKey，如果不同则忽略。
	 *
	 * @param mqMessageList 消息类型
	 * @return 结果
	 * @since 2024.05
	 */
	SendBatchResult sendBatch(final List<MqMessage> mqMessageList);

	/**
	 * 单向发送消息-批量
	 *
	 * @param mqMessageList 消息类型
	 * @return 结果
	 * @since 2024.05
	 */
	SendBatchResult sendOneWayBatch(final List<MqMessage> mqMessageList);

}
