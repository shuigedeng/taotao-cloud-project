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
package com.taotao.cloud.common.model;

import com.taotao.cloud.common.enums.EventEnum;
import com.taotao.cloud.common.model.Callable.Action1;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 发布订阅
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:38:42
 */
public class Pubsub<T> {

	/**
	 * subscribeList
	 */
	private final Map<String, ConcurrentHashMap<String, Sub<T>>> subscribeList = new ConcurrentHashMap<>();

	/**
	 * lock
	 */
	private final Object lock = new Object();

	/**
	 * pub
	 *
	 * @param event event
	 * @param data  data
	 * @author shuigedeng
	 * @since 2021-09-02 20:39:11
	 */
	public void pub(String event, T data) {
		ConcurrentHashMap<String, Sub<T>> subs = subscribeList.get(event);
		if (subs != null) {
			for (Map.Entry<String, Sub<T>> sub : subs.entrySet()) {
				try {
					sub.getValue().action.invoke(data);
				} catch (Exception e) {
					LogUtil.error(e, "分发订阅失败");
				}
			}
		}
	}

	/**
	 * sub
	 *
	 * @param event  event
	 * @param action action
	 * @author shuigedeng
	 * @since 2021-09-02 20:39:20
	 */
	private void sub(String event, Sub<T> action) {
		if (!subscribeList.containsKey(event)) {
			synchronized (lock) {
				if (!subscribeList.containsKey(event)) {
					subscribeList.putIfAbsent(event, new ConcurrentHashMap<>(1));
				}
			}
		}
		subscribeList.get(event).putIfAbsent(action.name, action);
	}

	/**
	 * sub
	 *
	 * @param event  event
	 * @param action action
	 * @author shuigedeng
	 * @since 2021-09-02 20:39:29
	 */
	public void sub(EventEnum event, Sub<T> action) {
		sub(event.toString(), action);
	}

	/**
	 * removeSub
	 *
	 * @param event   event
	 * @param subName subName
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:39:38
	 */
	public boolean removeSub(String event, String subName) {
		ConcurrentHashMap<String, Sub<T>> subs = subscribeList.get(event);
		if (subs != null) {
			subs.remove(subName);
			if (subs.size() == 0) {
				subscribeList.remove(event);
			}
			return true;
		}
		return false;
	}

	/**
	 * Pubsub
	 *
	 * @param <T> T
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:39:45
	 */
	public static class Sub<T> {

		/**
		 * name
		 */
		private String name;

		/**
		 * action
		 */
		private Callable.Action1<T> action;

		public Sub(String name, Action1<T> action) {
			this.name = name;
			this.action = action;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Action1<T> getAction() {
			return action;
		}

		public void setAction(Action1<T> action) {
			this.action = action;
		}
	}

	public Map<String, ConcurrentHashMap<String, Sub<T>>> getSubscribeList() {
		return subscribeList;
	}

	public Object getLock() {
		return lock;
	}

}
