/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.disruptor.context;


import com.taotao.cloud.disruptor.event.DisruptorEvent;
import java.lang.reflect.Constructor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

/**
 * EventPublicationInterceptor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:06:12
 */
public class EventPublicationInterceptor
		implements MethodInterceptor, DisruptorEventPublisherAware, InitializingBean {

	private Constructor<?> applicationEventClassConstructor;

	private DisruptorEventPublisher applicationEventPublisher;

	/**
	 * Set the application event class to publish.
	 * <p>The event class <b>must</b> have a constructor with a single
	 * {@code Object} argument for the event source. The interceptor
	 * will pass in the invoked object.
	 * @param applicationEventClass the application event class
	 * @throws IllegalArgumentException if the supplied {@code Class} is
	 * {@code null} or if it is not an {@code ApplicationEvent} subclass or
	 * if it does not expose a constructor that takes a single {@code Object} argument
	 */
	public void setApplicationEventClass(Class<?> applicationEventClass) {
		if (DisruptorEvent.class == applicationEventClass || !DisruptorEvent.class.isAssignableFrom(applicationEventClass)) {
			throw new IllegalArgumentException("applicationEventClass needs to extend DisruptorEvent");
		}
		try {
			this.applicationEventClassConstructor = applicationEventClass.getConstructor( new Class<?>[] {Object.class} );
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("applicationEventClass [" +
					applicationEventClass.getName() + "] does not have the required Object constructor: " + ex);
		}
	}

	@Override
	public void setDisruptorEventPublisher(DisruptorEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.applicationEventClassConstructor == null) {
			throw new IllegalArgumentException("applicationEventClass is required");
		}
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object retVal = invocation.proceed();
		DisruptorEvent event = (DisruptorEvent) this.applicationEventClassConstructor.newInstance(new Object[] {invocation.getThis()});
		this.applicationEventPublisher.publishEvent(event);
		return retVal;
	}

}
