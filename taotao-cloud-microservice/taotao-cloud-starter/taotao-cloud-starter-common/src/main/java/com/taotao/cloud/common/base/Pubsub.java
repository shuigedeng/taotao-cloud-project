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
package com.taotao.cloud.common.base;

import com.taotao.cloud.common.enums.EventEnum;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

/**
 * 发布订阅
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:09
 **/
public class Pubsub {

	public static Pubsub Default = new Pubsub();
	private final Map<String, ConcurrentHashMap<String, Sub>> subscribeList = new ConcurrentHashMap<>();
	private final Object lock = new Object();

	public <T> void pub(String event, T data) {
		ConcurrentHashMap<String, Sub> subs = subscribeList.get(event);
		if (subs != null) {
			for (Map.Entry<String, Sub> sub : subs.entrySet()) {
				try {
					sub.getValue().action.invoke(data);
				} catch (Exception e) {
					LogUtil.error("分发订阅失败", e);
				}
			}
		}
	}

	private <T> void sub(String event, Sub<T> action) {
		if (!subscribeList.containsKey(event)) {
			synchronized (lock) {
				if (!subscribeList.containsKey(event)) {
					subscribeList.putIfAbsent(event, new ConcurrentHashMap());
				}
			}
		}
		subscribeList.get(event).putIfAbsent(action.name, action);
	}

	public <T> void sub(EventEnum event, Sub<T> action) {
		sub(event.toString(), action);
	}

	public boolean removeSub(String event, String subName) {
		val subs = subscribeList.get(event);
		if (subs != null) {
			subs.remove(subName);
			if (subs.size() == 0) {
				subscribeList.remove(event);
			}
			return true;
		}
		return false;
	}

	@Data
	@AllArgsConstructor
	public static class Sub<T> {

		private String name;
		private Callable.Action1<T> action;
	}
}
