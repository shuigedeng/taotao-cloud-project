/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.disruptor.handler;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.exception.EventHandleException;
import java.io.IOException;

/**
 * AbstractRouteableEventHandler
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:18:19
 */
public class AbstractRouteableEventHandler<T extends DisruptorEvent> extends
	AbstractEnabledEventHandler<T> {

	/**
	 * 用来判定使用那个HandlerChian
	 */
	protected HandlerChainResolver<T> handlerChainResolver;

	public AbstractRouteableEventHandler() {
		super();
	}

	public AbstractRouteableEventHandler(HandlerChainResolver<T> handlerChainResolver) {
		super();
		this.handlerChainResolver = handlerChainResolver;
	}

	@Override
	protected void doHandlerInternal(T event, HandlerChain<T> handlerChain) throws Exception {
		Throwable t = null;
		try {
			this.executeChain(event, handlerChain);
		} catch (Throwable throwable) {
			t = throwable;
		}
		if (t != null) {
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			String msg = "Handlered event failed.";
			throw new EventHandleException(msg, t);
		}
	}

	/**
	 * getExecutionChain
	 *
	 * @param event     event
	 * @param origChain origChain
	 * @return {@link com.taotao.cloud.disruptor.handler.HandlerChain }
	 * @author shuigedeng
	 * @since 2021-09-03 20:18:52
	 */
	protected HandlerChain<T> getExecutionChain(T event, HandlerChain<T> origChain) {
		HandlerChain<T> chain = origChain;

		HandlerChainResolver<T> resolver = getHandlerChainResolver();
		if (resolver == null) {
			LogUtils.debug("No HandlerChainResolver configured.  Returning original HandlerChain.");
			return origChain;
		}

		HandlerChain<T> resolved = resolver.getChain(event, origChain);
		if (resolved != null) {
			LogUtils.info("Resolved a configured HandlerChain for the current event.");
			chain = resolved;
		} else {
			LogUtils.info("No HandlerChain configured for the current event.  Using the default.");
		}

		return chain;
	}

	/**
	 * executeChain
	 *
	 * @param event     event
	 * @param origChain origChain
	 * @author shuigedeng
	 * @since 2021-09-03 20:18:43
	 */
	protected void executeChain(T event, HandlerChain<T> origChain) throws Exception {
		HandlerChain<T> chain = getExecutionChain(event, origChain);
		chain.doHandler(event);
	}

	public HandlerChainResolver<T> getHandlerChainResolver() {
		return handlerChainResolver;
	}

	public void setHandlerChainResolver(HandlerChainResolver<T> handlerChainResolver) {
		this.handlerChainResolver = handlerChainResolver;
	}


}
