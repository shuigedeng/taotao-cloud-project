package com.taotao.cloud.core.model;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.core.model.Callable.Action1;
import com.taotao.cloud.core.enums.EventEnum;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: chejiangyi
 * @version: 2019-08-10 13:44 发布订阅
 **/
public class Pubsub {

	public static Pubsub DEFAULT = new Pubsub();
	private Map<String, ConcurrentHashMap<String, Sub>> subscribeList = new ConcurrentHashMap<>();
	private Object lock = new Object();

	public <T> void pub(String event, T data) {
		ConcurrentHashMap<String, Sub> subs = subscribeList.get(event);
		if (subs != null) {
			for (Map.Entry<String, Sub> sub : subs.entrySet()) {
				try {
					sub.getValue().action.invoke(data);
				} catch (Exception e) {
					LogUtil.error(e,"分发订阅失败");
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
		ConcurrentHashMap<String, Sub> subs = subscribeList.get(event);
		if (subs != null) {
			subs.remove(subName);
			if (subs.size() == 0) {
				subscribeList.remove(event);
			}
			return true;
		}
		return false;
	}

	public static class Sub<T> {

		private String name;
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
}
