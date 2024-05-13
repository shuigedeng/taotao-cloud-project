package com.taotao.cloud.rpc.common.factory;


import java.util.HashMap;
import java.util.Map;

public class SingleFactory {

	private static Map<Class, Object> objectMap = new HashMap<>();

	private SingleFactory() {
	}

	/**
	 * 使用 双重 校验锁 实现 单例模式
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getInstance(Class<T> clazz) {
		Object instance = objectMap.get(clazz);
		if (instance == null) {
			synchronized (clazz) {
				if (instance == null) {
					try {
						instance = clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			}
		}
		return clazz.cast(instance);
	}

}
