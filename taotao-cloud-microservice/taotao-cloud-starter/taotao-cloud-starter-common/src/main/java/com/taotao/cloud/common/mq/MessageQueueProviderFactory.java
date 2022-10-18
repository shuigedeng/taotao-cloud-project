package com.taotao.cloud.common.mq;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;

/**
 * 消息队列生产者实例工厂
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
