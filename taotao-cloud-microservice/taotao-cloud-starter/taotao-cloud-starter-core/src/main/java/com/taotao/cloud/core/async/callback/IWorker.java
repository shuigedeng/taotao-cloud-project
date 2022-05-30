package com.taotao.cloud.core.async.callback;

import com.taotao.cloud.core.async.wrapper.WorkerWrapper;
import java.util.Map;

/**
 * 每个最小执行单元需要实现该接口
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:24:06
 */
@FunctionalInterface
public interface IWorker<T, V> {

	/**
	 * 在这里做耗时操作，如rpc请求、IO等
	 *
	 * @param object      object
	 * @param allWrappers 任务包装
	 * @return {@link V }
	 * @since 2022-05-30 13:24:06
	 */
	V action(T object, Map<String, WorkerWrapper> allWrappers);

	/**
	 * 超时、异常时，返回的默认值
	 *
	 * @return {@link V }
	 * @since 2022-05-30 13:24:06
	 */
	default V defaultValue() {
        return null;
    }
}
