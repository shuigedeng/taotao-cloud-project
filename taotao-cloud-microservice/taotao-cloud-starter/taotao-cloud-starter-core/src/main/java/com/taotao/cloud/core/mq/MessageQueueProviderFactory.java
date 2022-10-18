package com.taotao.cloud.core.mq;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息队列生产者实例工厂
 */
public class MessageQueueProviderFactory {

	private static final Map<String, String> beanSettings = new ConcurrentHashMap<>();

	private final String defaultType;

	public MessageQueueProviderFactory(String defaultType) {
		this.defaultType = defaultType;
	}

	public static void addBean(String type, String beanName) {
		beanSettings.put(type, beanName);
	}

	public MessageQueueProvider getProvider() {
		MessageQueueProvider messageQueueProvider = StringUtils.isNotBlank(defaultType) ?
			ContextUtils.getBean(beanSettings.get(defaultType.toUpperCase())) :
			ContextUtils.getBean(MessageQueueProvider.class);
		Assert.notNull(messageQueueProvider, "MessageQueueProvider beanDefinition not found");
		return messageQueueProvider;
	}

	public MessageQueueProvider getProvider(String type) {
		String beanName = beanSettings.get(type.toUpperCase());
		MessageQueueProvider messageQueueProvider = ContextUtils.getBean(beanName,
			MessageQueueProvider.class);
		Assert.notNull(messageQueueProvider,
			"MessageQueueProvider beanDefinition named '" + beanName + "' not found");
		return messageQueueProvider;
	}
}
