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
 * HandlerChain
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:20:37
 */
public interface HandlerChain<T extends DisruptorEvent> {

	/**
	 * 做处理程序
	 *
	 * @param event 事件
	 * @since 2022-05-16 13:53:09
	 */
	void doHandler(T event) throws Exception;

}
