/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.model.Callable;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.core.monitor.Monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 提供线程池操作类 默认使用自定义的全局线程池
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:57:25
 */
public class ThreadUtil extends cn.hutool.core.thread.ThreadUtil {

	/**
	 * 使用系统线程池并行for循环
	 *
	 * @param taskName      任务名称
	 * @param parallelCount 并行数量
	 * @param taskList      任务列表
	 * @param action        action
	 * @since 2021-09-02 20:57:35
	 */
	public static <T> void parallelFor(String taskName, int parallelCount, Collection<T> taskList,
									   final Callable.Action1<T> action) {
		if (parallelCount < 2) {
			for (T t : taskList) {
				action.invoke(t);
			}
		} else {
			Monitor monitorThreadPool = ContextUtils.getBean(Monitor.class, false);
			monitorThreadPool.monitorParallelFor2(taskName, parallelCount, taskList, action);
		}
	}

	/**
	 * 线程池分批执行某个方法（batchExecute(temList,storeCode1)），所有数据都执行完成后返回数据
	 *
	 * @param pageSize   页面大小
	 * @param timeout    超时(分组)
	 * @param dataList   数据列表
	 * @param middleFunc 中间函数
	 * @param resultFunc 结果函数
	 * @return {@link List }<{@link R }>
	 * @since 2022-09-15 13:05:39
	 */
	public static <R, M, D> List<R> batchExecute(int pageSize, long timeout, List<D> dataList, java.util.function.Function<List<D>, M> middleFunc, java.util.function.Function<M, R> resultFunc) {
		int totalSize = dataList.size();
		int totalPage = totalSize / pageSize;

		ExecutorService pool = new ThreadPoolExecutor(totalPage + 1, totalPage + 1,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

		List<Future<M>> futureList = new ArrayList<>();
		for (int pageNum = 1; pageNum <= totalPage + 1; pageNum++) {
			int starNum = (pageNum - 1) * pageSize;
			int endNum = Math.min(pageNum * pageSize, totalSize);
			List<D> temList = dataList.subList(starNum, endNum);

			if (CollectionUtil.isNotEmpty(temList)) {
				java.util.concurrent.Callable<M> callable = () -> middleFunc.apply(temList);
				Future<M> future = pool.submit(callable);
				futureList.add(future);
			}
		}
		pool.shutdown(); // 不允许再想线程池中增加线程

		List<R> result = new ArrayList<>();
		try {
			//判断是否所有线程已经执行完毕
			boolean isFinish = pool.awaitTermination(timeout, TimeUnit.MINUTES);
			//如果没有执行完
			if (!isFinish) {
				//线程池执行结束 不在等待线程执行完毕，直接执行下面的代码
				pool.shutdownNow();
			}

			result = futureList.stream().map(e -> {
				try {
					return resultFunc.apply(e.get());
				} catch (InterruptedException | ExecutionException ex) {
					throw new RuntimeException(ex);
				}
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
