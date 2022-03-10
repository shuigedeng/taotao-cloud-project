/*
 * Copyright 2002-2021 the original author or authors.
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

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * AbstractAdviceEventHandler
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:14:12
 */
public class AbstractAdviceEventHandler<T extends DisruptorEvent> extends
	AbstractEnabledEventHandler<T> {

	/**
	 * preHandle
	 *
	 * @param event event
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:14:42
	 */
	protected boolean preHandle(T event) throws Exception {
		return true;
	}

	/**
	 * postHandle
	 *
	 * @param event event
	 * @author shuigedeng
	 * @since 2021-09-03 20:14:45
	 */
	protected void postHandle(T event) throws Exception {
	}

	/**
	 * afterCompletion
	 *
	 * @param event     event
	 * @param exception exception
	 * @author shuigedeng
	 * @since 2021-09-03 20:14:47
	 */
	public void afterCompletion(T event, Exception exception) throws Exception {
	}

	/**
	 * executeChain
	 *
	 * @param event event
	 * @param chain chain
	 * @author shuigedeng
	 * @since 2021-09-03 20:14:50
	 */
	protected void executeChain(T event, HandlerChain<T> chain) throws Exception {
		chain.doHandler(event);
	}

	@Override
	public void doHandlerInternal(T event, HandlerChain<T> handlerChain) throws Exception {

		if (!isEnabled(event)) {
			LogUtil.debug(
				"Handler '{}' is not enabled for the current event.  Proceeding without invoking this handler.",
				getName());
			// Proceed without invoking this handler...
			handlerChain.doHandler(event);
		} else {
			LogUtil.info("Handler '{}' enabled.  Executing now.", getName());

			Exception exception = null;
			try {

				boolean continueChain = preHandle(event);
				LogUtil.info(
					"Invoked preHandle method.  Continuing chain?: [" + continueChain + "]");
				if (continueChain) {
					executeChain(event, handlerChain);
				}
				postHandle(event);
				LogUtil.info("Successfully invoked postHandle method");
			} catch (Exception e) {
				exception = e;
			} finally {
				cleanup(event, exception);
			}
		}
	}

	protected void cleanup(T event, Exception existing) throws Exception {
		Exception exception = existing;
		try {
			afterCompletion(event, exception);
			LogUtil.info("Successfully invoked afterCompletion method.");
		} catch (Exception e) {
			if (exception == null) {
				exception = e;
			} else {
				LogUtil.debug(
					"afterCompletion implementation threw an exception.  This will be ignored to "
						+ "allow the original source exception to be propagated.", e);
			}
		}
	}

	/**
	 * isEnabled
	 *
	 * @param event event
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:16:17
	 */
	protected boolean isEnabled(T event)
		throws Exception {
		return isEnabled();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
