package com.taotao.cloud.order.biz.roketmq.listener;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.stream.framework.rocketmq.tags.OrderTagsEnum;
import java.util.List;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单消息
 **/
@Component
@RocketMQMessageListener(topic = "${taotao.data.rocketmq.order-topic}", consumerGroup = "${taotao.data.rocketmq.order-group}")
public class OrderMessageListener implements RocketMQListener<MessageExt> {

	/**
	 * 交易
	 */
	@Autowired
	private List<TradeEvent> tradeEvent;
	/**
	 * 订单状态
	 */
	@Autowired
	private List<OrderStatusChangeEvent> orderStatusChangeEvents;
	/**
	 * 缓存
	 */
	@Autowired
	private RedisRepository redisRepository;

	@Override
	public void onMessage(MessageExt messageExt) {
		try {
			this.orderStatusEvent(messageExt);
		} catch (Exception e) {
			LogUtil.error("订单状态变更事件调用异常", e);
		}
	}

	/**
	 * 订单状态变更
	 *
	 * @param messageExt
	 */
	public void orderStatusEvent(MessageExt messageExt) {

		switch (OrderTagsEnum.valueOf(messageExt.getTags())) {
			//订单创建
			case ORDER_CREATE:
				String key = new String(messageExt.getBody());
				TradeDTO tradeDTO = JSONUtil.toBean(redisRepository.get(key).toString(),
					TradeDTO.class);
				boolean result = true;
				for (TradeEvent event : tradeEvent) {
					try {
						event.orderCreate(tradeDTO);
					} catch (Exception e) {
						LogUtil.error("交易{}入库,在{}业务中，状态修改事件执行异常",
							tradeDTO.getSn(),
							event.getClass().getName(),
							e);
						result = false;
					}
				}
				//如所有步骤顺利完成
				if (Boolean.TRUE.equals(result)) {
					//清除记录信息的trade cache key
					redisRepository.del(key);
				}
				break;
			//订单状态变更
			case STATUS_CHANGE:
				for (OrderStatusChangeEvent orderStatusChangeEvent : orderStatusChangeEvents) {
					try {
						OrderMessage orderMessage = JSONUtil.toBean(
							new String(messageExt.getBody()), OrderMessage.class);
						orderStatusChangeEvent.orderChange(orderMessage);
					} catch (Exception e) {
						LogUtil.error("订单{},在{}业务中，状态修改事件执行异常",
							new String(messageExt.getBody()),
							orderStatusChangeEvent.getClass().getName(),
							e);
					}
				}
				break;
			default:
				break;
		}
	}
}
