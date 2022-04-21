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

import com.lmax.disruptor.EventHandler;
import com.taotao.cloud.disruptor.event.DisruptorEvent;
import org.springframework.core.Ordered;


/**
 * DisruptorEventDispatcher
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:20:08
 */
public class DisruptorEventDispatcher extends
	AbstractRouteableEventHandler<DisruptorEvent> implements
	EventHandler<DisruptorEvent>, Ordered {

	private int order;

	public DisruptorEventDispatcher(HandlerChainResolver<DisruptorEvent> filterChainResolver,
		int order) {
		super(filterChainResolver);
		this.order = order;
	}

	/*
	 * 责任链入口
	 */
	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {

		//构造原始链对象
		HandlerChain<DisruptorEvent> originalChain = new ProxiedHandlerChain();
		//执行事件处理链
		this.doHandler(event, originalChain);

	}

	@Override
	public int getOrder() {
		return order;
	}

}

