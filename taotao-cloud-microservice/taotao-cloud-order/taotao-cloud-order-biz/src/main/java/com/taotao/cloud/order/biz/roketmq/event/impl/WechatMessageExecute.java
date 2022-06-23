package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.web.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.message.OrderMessage;
import com.taotao.cloud.order.api.web.vo.order.OrderVO;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信消息执行器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:12:33
 */
@Service
public class WechatMessageExecute implements OrderStatusChangeEvent, TradeEvent {

	@Autowired
	private WechatMessageUtil wechatMessageUtil;

	@Override
	public void orderCreate(TradeDTO tradeDTO) {
		for (OrderVO orderVO : tradeDTO.getOrderVO()) {
			try {
				wechatMessageUtil.sendWechatMessage(orderVO.getSn());
			} catch (Exception e) {
				LogUtil.error("微信消息发送失败：" + orderVO.getSn(), e);
			}
		}
	}

	@Override
	public void orderChange(OrderMessage orderMessage) {
		switch (orderMessage.newStatus()) {
			case PAID:
			case UNDELIVERED:
			case DELIVERED:
			case COMPLETED:
				try {
					wechatMessageUtil.sendWechatMessage(orderMessage.getOrderSn());
				} catch (Exception e) {
					LogUtil.error("微信消息发送失败", e);
				}
				break;
			default:
				break;
		}

	}
}
