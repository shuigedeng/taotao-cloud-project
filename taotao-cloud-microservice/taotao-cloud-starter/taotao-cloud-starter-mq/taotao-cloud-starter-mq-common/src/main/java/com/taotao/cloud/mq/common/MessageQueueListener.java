package com.taotao.cloud.mq.common;

import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.mq.common.consumer.MessageModel;
import com.taotao.cloud.mq.common.consumer.MessageSelectorType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息队列监听注解
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageQueueListener {

	/**
	 * 消息队列类型
	 *
	 * @return 消息队列类型
	 */
	String type() default StringUtils.EMPTY;

	/**
	 * 设置消费者组
	 *
	 * @return 消费者组名
	 */
	String group() default StringUtils.EMPTY;

	/**
	 * 设置消息主题
	 *
	 * @return 消息主题
	 */
	String topic() default StringUtils.EMPTY;

	/**
	 * 批量拉取消息大小
	 *
	 * @return 拉取大小
	 */
	int pullBatchSize() default 0;

	/**
	 * 消费消息批次上限大小，当拉取消息的大小大于消费的大小时，拆成多个线程并发处理
	 */
	int consumeMessageBatchMaxSize() default 0;

	/**
	 * 消息模式
	 *
	 * @return 消息模式
	 */
	String messageModel() default MessageModel.CLUSTERING;

	/**
	 * 消息过滤类型
	 *
	 * @return 消息过滤类型
	 */
	String selectorType() default MessageSelectorType.TAG;

	/**
	 * 消息过滤规则
	 *
	 * @return 消息过滤规则
	 */
	String selectorExpression() default "*";
}
