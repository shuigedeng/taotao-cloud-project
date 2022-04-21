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

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.disruptor.event.DisruptorEvent;
import java.util.List;

/**
 * ProxiedHandlerChain
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:23:39
 */
public class ProxiedHandlerChain implements HandlerChain<DisruptorEvent> {

	private ProxiedHandlerChain originalChain;
	private List<DisruptorHandler<DisruptorEvent>> handlers;
	private int currentPosition = 0;

	public ProxiedHandlerChain() {
		this.currentPosition = -1;
	}

	public ProxiedHandlerChain(ProxiedHandlerChain orig,
		List<DisruptorHandler<DisruptorEvent>> handlers) {
		if (orig == null) {
			throw new NullPointerException("original HandlerChain cannot be null.");
		}
		this.originalChain = orig;
		this.handlers = handlers;
		this.currentPosition = 0;
	}

	@Override
	public void doHandler(DisruptorEvent event) throws Exception {
		if (this.handlers == null || this.handlers.size() == this.currentPosition) {
			LogUtil.info("Invoking original filter chain.");
			if (this.originalChain != null) {
				this.originalChain.doHandler(event);
			}
		} else {
			LogUtil.info("Invoking wrapped filter at index [" + this.currentPosition + "]");
			this.handlers.get(this.currentPosition++).doHandler(event, this);
		}
	}

}
