package com.taotao.cloud.core.async.callback;


import com.taotao.cloud.core.async.worker.WorkResult;

/**
 * 每个执行单元执行完毕后，会回调该接口</p> 需要监听执行结果的，实现该接口即可
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:23:32
 */
@FunctionalInterface
public interface ICallback<T, V> {

	/**
	 * 任务开始的监听
	 *
	 * @since 2022-05-30 13:23:15
	 */
	default void begin() {

    }

	/**
	 * 耗时操作执行完毕后，就给value注入值
	 *
	 * @param success    成功
	 * @param param      参数
	 * @param workResult 工作结果
	 * @since 2022-05-30 13:23:16
	 */
	void result(boolean success, T param, WorkResult<V> workResult);
}
