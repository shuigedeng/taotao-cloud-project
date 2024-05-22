package com.taotao.cloud.mq.client.producer.api;


import com.taotao.cloud.mq.client.producer.dto.SendBatchResult;
import com.taotao.cloud.mq.client.producer.dto.SendResult;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqProducer {

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
	 * 同步发送消息-批量
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
