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


import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * HandlerChainResolver
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:21:59
 */
public interface HandlerChainResolver<T extends DisruptorEvent> {

	/**
	 * 得到链
	 *
	 * @param event         事件
	 * @param originalChain 原来链
	 * @return {@link HandlerChain }<{@link T }>
	 * @since 2022-05-16 13:53:16
	 */
	HandlerChain<T> getChain(T event, HandlerChain<T> originalChain);

}
