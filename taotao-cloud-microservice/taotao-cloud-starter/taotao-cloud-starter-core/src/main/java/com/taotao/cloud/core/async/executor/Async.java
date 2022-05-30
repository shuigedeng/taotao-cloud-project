package com.taotao.cloud.core.async.executor;


import com.taotao.cloud.core.async.callback.DefaultGroupCallback;
import com.taotao.cloud.core.async.callback.IGroupCallback;
import com.taotao.cloud.core.async.wrapper.WorkerWrapper;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 类入口，可以根据自己情况调整core线程的数量
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:24:28
 */
public class Async {

	/**
	 * 常见池 默认不定长线程池
	 */
	private static final ThreadPoolExecutor COMMON_POOL = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	/**
	 * 遗嘱执行人服务 注意，这里是个static，也就是只能有一个线程池。用户自定义线程池时，也只能定义一个
	 */
	private static ExecutorService executorService;

	/**
	 * 出发点
	 *
	 * @param timeout
	 * @param executorService 遗嘱执行人服务
	 * @param workerWrappers  工人包装
	 * @return boolean
	 * @since 2022-05-30 13:24:33
	 */
	@SuppressWarnings("unchecked")
    public static boolean beginWork(long timeout, ExecutorService executorService, List<WorkerWrapper> workerWrappers) throws ExecutionException, InterruptedException {
        if(workerWrappers == null || workerWrappers.size() == 0) {
            return false;
        }
        //保存线程池变量
        Async.executorService = executorService;
        //定义一个map，存放所有的wrapper，key为wrapper的唯一id，value是该wrapper，可以从value中获取wrapper的result
        Map<String, WorkerWrapper> forParamUseWrappers = new ConcurrentHashMap<>();
        CompletableFuture[] futures = new CompletableFuture[workerWrappers.size()];
        for (int i = 0; i < workerWrappers.size(); i++) {
            WorkerWrapper wrapper = workerWrappers.get(i);
            futures[i] = CompletableFuture.runAsync(() -> wrapper.work(executorService, timeout, forParamUseWrappers), executorService);
        }

        try {
            CompletableFuture.allOf(futures).get(timeout, TimeUnit.MILLISECONDS);
            return true;
        } catch (TimeoutException e) {
            Set<WorkerWrapper> set = new HashSet<>();
            totalWorkers(workerWrappers, set);
            for (WorkerWrapper wrapper : set) {
                wrapper.stopNow();
            }
            return false;
        }
    }

	/**
	 * 如果想自定义线程池，请传pool。不自定义的话，就走默认的COMMON_POOL
	 *
	 * @param timeout         超时
	 * @param executorService 遗嘱执行人服务
	 * @param workerWrapper
	 * @return boolean
	 * @since 2022-05-30 13:24:35
	 */
	public static boolean beginWork(long timeout, ExecutorService executorService, WorkerWrapper... workerWrapper) throws ExecutionException, InterruptedException {
        if(workerWrapper == null || workerWrapper.length == 0) {
            return false;
        }
        List<WorkerWrapper> workerWrappers =  Arrays.stream(workerWrapper).collect(Collectors.toList());
        return beginWork(timeout, executorService, workerWrappers);
    }

	/**
	 * 同步阻塞,直到所有都完成,或失败
	 *
	 * @param timeout       超时
	 * @param workerWrapper 工人包装
	 * @return boolean
	 * @since 2022-05-30 13:24:36
	 */
	public static boolean beginWork(long timeout, WorkerWrapper... workerWrapper) throws ExecutionException, InterruptedException {
        return beginWork(timeout, COMMON_POOL, workerWrapper);
    }

	/**
	 * 开始异步工作
	 *
	 * @param timeout       超时
	 * @param groupCallback 组回调
	 * @param workerWrapper 工人包装
	 * @since 2022-05-30 13:24:36
	 */
	public static void beginWorkAsync(long timeout, IGroupCallback groupCallback, WorkerWrapper... workerWrapper) {
        beginWorkAsync(timeout, COMMON_POOL, groupCallback, workerWrapper);
    }

	/**
	 * 异步执行,直到所有都完成,或失败后，发起回调
	 *
	 * @param timeout         超时
	 * @param executorService 遗嘱执行人服务
	 * @param groupCallback   组回调
	 * @param workerWrapper   工人包装
	 * @since 2022-05-30 13:24:36
	 */
	public static void beginWorkAsync(long timeout, ExecutorService executorService, IGroupCallback groupCallback, WorkerWrapper... workerWrapper) {
        if (groupCallback == null) {
            groupCallback = new DefaultGroupCallback();
        }
        IGroupCallback finalGroupCallback = groupCallback;
        if (executorService != null) {
            executorService.submit(() -> {
                try {
                    boolean success = beginWork(timeout, executorService, workerWrapper);
                    if (success) {
                        finalGroupCallback.success(Arrays.asList(workerWrapper));
                    } else {
                        finalGroupCallback.failure(Arrays.asList(workerWrapper), new TimeoutException());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    finalGroupCallback.failure(Arrays.asList(workerWrapper), e);
                }
            });
        } else {
            COMMON_POOL.submit(() -> {
                try {
                    boolean success = beginWork(timeout, COMMON_POOL, workerWrapper);
                    if (success) {
                        finalGroupCallback.success(Arrays.asList(workerWrapper));
                    } else {
                        finalGroupCallback.failure(Arrays.asList(workerWrapper), new TimeoutException());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    finalGroupCallback.failure(Arrays.asList(workerWrapper), e);
                }
            });
        }

    }

	/**
	 * 总共多少个执行单元
	 *
	 * @param workerWrappers 工人包装
	 * @param set            集
	 * @since 2022-05-30 13:24:36
	 */
	@SuppressWarnings("unchecked")
    private static void totalWorkers(List<WorkerWrapper> workerWrappers, Set<WorkerWrapper> set) {
        set.addAll(workerWrappers);
        for (WorkerWrapper wrapper : workerWrappers) {
            if (wrapper.getNextWrappers() == null) {
                continue;
            }
            List<WorkerWrapper> wrappers = wrapper.getNextWrappers();
            totalWorkers(wrappers, set);
        }

    }

	/**
	 * 关闭线程池
	 *
	 * @since 2022-05-30 13:24:36
	 */
	public static void shutDown() {
        shutDown(executorService);
    }

	/**
	 * 关闭线程池
	 *
	 * @param executorService 遗嘱执行人服务
	 * @since 2022-05-30 13:24:36
	 */
	public static void shutDown(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdown();
        } else {
            COMMON_POOL.shutdown();
        }
    }

	/**
	 * 获取线程数量
	 *
	 * @return {@link String }
	 * @since 2022-05-30 13:24:37
	 */
	public static String getThreadCount() {
        return "activeCount=" + COMMON_POOL.getActiveCount() +
                "  completedCount " + COMMON_POOL.getCompletedTaskCount() +
                "  largestCount " + COMMON_POOL.getLargestPoolSize();
    }
}
