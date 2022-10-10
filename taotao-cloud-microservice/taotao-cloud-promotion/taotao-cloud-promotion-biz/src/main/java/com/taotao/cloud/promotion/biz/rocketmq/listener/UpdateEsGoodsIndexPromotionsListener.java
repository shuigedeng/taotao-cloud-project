package com.taotao.cloud.promotion.biz.rocketmq.listener;

import com.taotao.cloud.promotion.api.event.UpdateEsGoodsIndexPromotionsEvent;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UpdateEsGoodsIndexPromotionsListener {

	/**
	 * rocketMq
	 */
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	/**
	 * rocketMq配置
	 */
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;


	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
	public void updateEsGoodsIndexPromotions(UpdateEsGoodsIndexPromotionsEvent event) {
		//更新商品促销消息
		String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_PROMOTIONS.name();
		//发送mq消息
		rocketMQTemplate.asyncSend(destination, event.getPromotionsJsonStr(), RocketmqSendCallbackBuilder.commonCallback());
	}


}
