package com.taotao.cloud.sys.biz.event.transactional;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事务提交后发生mq事件
 *
 * <pre>
 *     {@code
 *      applicationEventPublisher.publishEvent(new TransactionCommitSendMQEvent("删除店铺商品", rocketmqCustomProperties.getGoodsTopic(), GoodsTagsEnum.STORE_GOODS_DELETE.name(), storeId));
 *     }
 * </pre>
 **/
public class TransactionCommitSendMQEvent extends ApplicationEvent {

	private static final long serialVersionUID = 5885956821347953071L;

	@Getter
	private final String topic;

	@Getter
	private final String tag;

	@Getter
	private final Object message;

	public TransactionCommitSendMQEvent(Object source, String topic, String tag, Object message) {
		super(source);
		this.topic = topic;
		this.tag = tag;
		this.message = message;
	}
}
