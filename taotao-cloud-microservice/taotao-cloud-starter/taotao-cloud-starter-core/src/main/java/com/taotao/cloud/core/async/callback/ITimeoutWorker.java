package com.taotao.cloud.core.async.callback;

/**
 * itimeout worker
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:23:55
 */
public interface ITimeoutWorker<T, V> extends IWorker<T, V> {

	/**
	 * 每个worker都可以设置超时时间
	 *
	 * @return long
	 * @since 2022-05-30 13:23:55
	 */
	long timeOut();

	/**
	 * 是否开启单个执行单元的超时功能（有时是一个group设置个超时，而不具备关心单个worker的超时）
	 * <p>注意，如果开启了单个执行单元的超时检测，将使线程池数量多出一倍</p>
	 *
	 * @return boolean
	 * @since 2022-05-30 13:23:55
	 */
	boolean enableTimeOut();
}
