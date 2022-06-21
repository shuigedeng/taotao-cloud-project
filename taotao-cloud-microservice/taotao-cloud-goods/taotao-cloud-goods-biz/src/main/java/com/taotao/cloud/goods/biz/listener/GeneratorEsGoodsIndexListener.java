package com.taotao.cloud.goods.biz.listener;

import com.taotao.cloud.goods.api.event.GeneratorEsGoodsIndexEvent;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 生成es商品索引监听器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:18:31
 */
@Component
public class GeneratorEsGoodsIndexListener {

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

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void generatorEsGoodsIndex(GeneratorEsGoodsIndexEvent esGoodsIndexEvent) {
		String destination = rocketmqCustomProperties.getGoodsTopic() + ":"
			+ GoodsTagsEnum.GENERATOR_GOODS_INDEX.name();

		//发送mq消息
		rocketMQTemplate.asyncSend(destination, esGoodsIndexEvent.getGoodsId(),
			RocketmqSendCallbackBuilder.commonCallback());
	}

}
