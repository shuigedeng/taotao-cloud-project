package com.taotao.cloud.common.base;

import com.taotao.cloud.common.enums.EventEnum;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

/**
 * @author: chejiangyi
 * @version: 2019-08-10 13:44 发布订阅
 **/
public class Pubsub {

	public static Pubsub Default = new Pubsub();
	private Map<String, ConcurrentHashMap<String, Sub>> subscribeList = new ConcurrentHashMap<>();
	private Object lock = new Object();

	public <T> void pub(String event, T data) {
		val subs = subscribeList.get(event);
		if (subs != null) {
			for (val sub : subs.entrySet()) {
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
