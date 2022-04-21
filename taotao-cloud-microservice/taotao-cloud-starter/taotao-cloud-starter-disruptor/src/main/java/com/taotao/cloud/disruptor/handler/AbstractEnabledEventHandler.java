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
package com.taotao.cloud.disruptor.handler;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * AbstractEnabledEventHandler
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:15:14
 */
public abstract class AbstractEnabledEventHandler<T extends DisruptorEvent> extends
	AbstractNameableEventHandler<T> {

	protected boolean enabled = true;

	protected abstract void doHandlerInternal(T event, HandlerChain<T> handlerChain)
		throws Exception;

	@Override
	public void doHandler(T event, HandlerChain<T> handlerChain) throws Exception {
		if (!isEnabled(event)) {
			LogUtil.debug(
				"Handler '{}' is not enabled for the current event.  Proceeding without invoking this handler.",
				getName());
			// Proceed without invoking this handler...
			handlerChain.doHandler(event);
		} else {
			LogUtil.info("Handler '{}' enabled.  Executing now.", getName());
			doHandlerInternal(event, handlerChain);
		}
	}

	protected boolean isEnabled(T event) throws Exception {
		return isEnabled();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}
