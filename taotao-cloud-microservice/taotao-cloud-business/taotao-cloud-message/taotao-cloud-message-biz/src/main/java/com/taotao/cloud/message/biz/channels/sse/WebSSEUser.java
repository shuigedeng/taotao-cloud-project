package com.taotao.cloud.message.biz.channels.sse;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSSEUser {
	private static Map<String, Chater> userChaterMap = new ConcurrentHashMap<>();

	public static void add(String userName, Chater chater) {
		userChaterMap.put(userName, chater);
	}

	/**
	 * 根据昵称拿Chater
	 * 
	 * @param nickName
	 * @return
	 */
	public static Chater getChater(String userName) {
		return userChaterMap.get(userName);
	}

	/**
	 * 移除失效的Chater
	 * 
	 * @param Chater
	 */
	public static void removeUser(String userName) {
		userChaterMap.remove(userName);
	}

	public static Set<String> getUserList() {
		return userChaterMap.keySet();
	}
}
