package com.taotao.cloud.websocket.netty.support;

import com.taotao.cloud.websocket.netty.annotation.OnEvent;
import io.netty.channel.Channel;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.MethodParameter;

public class EventMethodArgumentResolver implements MethodArgumentResolver {

	private AbstractBeanFactory beanFactory;

	public EventMethodArgumentResolver(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getMethod().isAnnotationPresent(OnEvent.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
		throws Exception {
		if (object == null) {
			return null;
		}
		TypeConverter typeConverter = beanFactory.getTypeConverter();
		return typeConverter.convertIfNecessary(object, parameter.getParameterType());
	}
}
