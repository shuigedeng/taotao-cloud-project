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
package com.taotao.cloud.disruptor.translator;

import com.lmax.disruptor.EventTranslatorThreeArg;
import com.taotao.cloud.disruptor.event.DisruptorEvent;

/**
 * DisruptorEventThreeArgTranslator
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:25:03
 */
public class DisruptorEventThreeArgTranslator implements
	EventTranslatorThreeArg<DisruptorEvent, String, String, String> {

	@Override
	public void translateTo(DisruptorEvent dtEevent, long sequence, String event, String tag,
		String key) {
		dtEevent.setEvent(event);
		dtEevent.setTag(tag);
		dtEevent.setKey(key);
	}

}
