package com.taotao.cloud.common.support.instance;

/**
 * 实例化对象的接口 1. 使用此类的 class 必须有无参构造器 2. 当前类出于测试阶段。
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:10:24
 */
public interface Instance {

	/**
	 * 获取对象的单例对象 1. 需要保证对象的线程安全性。 2. 只有在同一个分组返回的对象才会是单例，否则返回 newInstance()
	 *
	 * @param tClass    class 类型
	 * @param groupName 分组名称
	 * @param <T>       泛型
	 * @return 实例化对象
	 */
	<T> T singleton(final Class<T> tClass,
		final String groupName);

	/**
	 * 获取对象的单例对象 1. 需要保证对象的线程安全性。
	 *
	 * @param tClass class 类型
	 * @param <T>    泛型
	 * @return 实例化对象
	 */
	<T> T singleton(final Class<T> tClass);

	/**
	 * 获取每个线程内唯一的实例化对象 注意：可能会内存泄漏的场景。 (1) 只要这个线程对象被gc回收，就不会出现内存泄露，但在threadLocal设为null和线程结束这段时间不会被回收的，就发生了我们认为的内存泄露。
	 * 最要命的是线程对象不被回收的情况，这就发生了真正意义上的内存泄露。比如使用线程池的时候，线程结束是不会销毁的，会再次使用的。就可能出现内存泄露。
	 * 参考资料：https://www.cnblogs.com/onlywujun/p/3524675.html
	 *
	 * @param tClass class 类型
	 * @param <T>    泛型
	 * @return 实例化对象
	 * @see java.lang.ref.WeakReference 弱引用
	 */
	<T> T threadLocal(final Class<T> tClass);

	/**
	 * 多例对象，每次都是全新的创建
	 *
	 * @param tClass class 类型
	 * @param <T>    泛型
	 * @return 实例化对象
	 */
	<T> T multiple(final Class<T> tClass);

	/**
	 * 线程安全对象 1. 判断当前类是否拥有 注解，
	 * 如果有，则直接创建单例对象。如果不是，则创建多例对象。
	 *
	 * @param tClass class 类型
	 * @param <T>    泛型
	 * @return 实例化对象
	 */
	<T> T threadSafe(final Class<T> tClass);

}
