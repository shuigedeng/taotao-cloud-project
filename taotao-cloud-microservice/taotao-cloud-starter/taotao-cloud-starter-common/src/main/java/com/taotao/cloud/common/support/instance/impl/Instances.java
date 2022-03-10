package com.taotao.cloud.common.support.instance.impl;


/**
 * 实例化工具类 对于 {@link InstanceFactory} 的便于使用
 */
public final class Instances {

	private Instances() {
	}

	/**
	 * 静态方法单例
	 *
	 * @param tClass 类信息
	 * @param <T>    泛型
	 * @return 结果
	 * @since 0.1.21 correct name
	 */
	public static <T> T singleton(Class<T> tClass) {
		return InstanceFactory.getInstance().singleton(tClass);
	}

	/**
	 * 静态方法单例
	 *
	 * @param tClass    类信息
	 * @param groupName 分组名称
	 * @param <T>       泛型
	 * @return 结果
	 * @since 0.1.21 correct name
	 */
	public static <T> T singleton(Class<T> tClass, final String groupName) {
		return InstanceFactory.getInstance().singleton(tClass, groupName);
	}

	/**
	 * threadLocal 同一个线程对应的实例一致
	 *
	 * @param tClass class
	 * @param <T>    泛型
	 * @return 结果
	 */
	public static <T> T threadLocal(Class<T> tClass) {
		return InstanceFactory.getInstance().threadLocal(tClass);
	}

	/**
	 * {@link ThreadSafe} 线程安全标示的使用单例，或者使用多例
	 *
	 * @param tClass class
	 * @param <T>    泛型
	 * @return 结果
	 */
	public static <T> T threadSafe(Class<T> tClass) {
		return InstanceFactory.getInstance().threadSafe(tClass);
	}

	/**
	 * 多例
	 *
	 * @param tClass class
	 * @param <T>    泛型
	 * @return 结果
	 */
	public static <T> T multiple(Class<T> tClass) {
		return InstanceFactory.getInstance().multiple(tClass);
	}

}
