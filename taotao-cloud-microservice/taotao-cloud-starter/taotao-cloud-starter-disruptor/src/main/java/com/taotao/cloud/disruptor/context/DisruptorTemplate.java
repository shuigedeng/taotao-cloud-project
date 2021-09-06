/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.disruptor.context;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.taotao.cloud.disruptor.event.DisruptorBindEvent;
import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * DisruptorTemplate
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:04:48
 */
public class DisruptorTemplate {

	private Disruptor<DisruptorEvent> disruptor;
	private EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator;

	public DisruptorTemplate(
		Disruptor<DisruptorEvent> disruptor,
		EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator) {
		this.disruptor = disruptor;
		this.oneArgEventTranslator = oneArgEventTranslator;
	}

	/**
	 * publishEvent
	 *
	 * @param event event
	 * @author shuigedeng
	 * @since 2021-09-03 20:04:57
	 */
	public void publishEvent(DisruptorBindEvent event) {
		disruptor.publishEvent(oneArgEventTranslator, event);
	}

	/**
	 * publishEvent
	 *
	 * @param event event
	 * @param tag   tag
	 * @param body  body
	 * @author shuigedeng
	 * @since 2021-09-03 20:05:00
	 */
	public void publishEvent(String event, String tag, Object body) {
		DisruptorBindEvent bindEvent = new DisruptorBindEvent();
		bindEvent.setEvent(event);
		bindEvent.setTag(tag);
		bindEvent.setBody(body);
		disruptor.publishEvent(oneArgEventTranslator, bindEvent);
	}

	/**
	 * publishEvent
	 *
	 * @param event event
	 * @param tag   tag
	 * @param key   key
	 * @param body  body
	 * @author shuigedeng
	 * @since 2021-09-03 20:05:04
	 */
	public void publishEvent(String event, String tag, String key, Object body) {
		DisruptorBindEvent bindEvent = new DisruptorBindEvent();
		bindEvent.setEvent(event);
		bindEvent.setTag(tag);
		bindEvent.setKey(key);
		bindEvent.setBody(body);
		disruptor.publishEvent(oneArgEventTranslator, bindEvent);
	}


}
