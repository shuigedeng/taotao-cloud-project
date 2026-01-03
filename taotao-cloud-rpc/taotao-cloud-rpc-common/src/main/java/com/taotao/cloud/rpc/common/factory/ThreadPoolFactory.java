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

package com.taotao.cloud.rpc.common.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadPoolFactory
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class ThreadPoolFactory {

    /**
     * CORE_POOL_SIZE : 线程池 CPU 核数 MAXIMUM_POOL_SIZE ： 最大 的 线程数 BLOCKING_QUEUE_CAPACITY ： 阻塞 队列 容量 KEEP_ALIVE_TIMEOUT ：
     * 心跳（单位：每分钟）
     */
    // CPU 密集型 推荐 核心线程
    // private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    // IO 密集型 推荐 核心线程
    private static final int CORE_POOL_SIZE = 20;

    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int BLOCKING_QUEUE_CAPACITY = 400;
    private static final int KEEP_ALIVE_TIMEOUT = 1;

    private static Map<String, ExecutorService> threadPoolsMap = new ConcurrentHashMap<>();

    public static void main( String[] args ) {
        ExecutorService test = createDefaultThreadPool("test", null);
        System.out.println(test);
    }

    public static ExecutorService createDefaultThreadPool( String threadNamePrefix ) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool( String threadNamePrefix, Boolean daemon ) {
        /**
         * 第一次有效，下次 返回 首次值, 参数 2 支持 函数编程
         * @FunctionalInterface
         * public interface Function<T, R> {
         *  R apply(T t);
         *  k 代表 apply(T t) 中的 参数 t
         *  createThreadPool(threadNamePrefix, daemon) 代表 R 类型的返回值 即 Map<K,V> 的 V
         *  因为 Function<? super K, ? extends V> K 对应了 T , V 对应了 R
         *  而 public interface Map<K, V> {
         */
        ExecutorService pool =
                threadPoolsMap.computeIfAbsent(
                        threadNamePrefix, k -> createThreadPool(threadNamePrefix, daemon));
        if (pool.isShutdown() || pool.isTerminated()) {
            threadPoolsMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            threadPoolsMap.put(threadNamePrefix, pool);
        }
        return pool;
    }

    public static void shutdownAll() {
        log.info("close all ThreadPool now ...");
        threadPoolsMap.entrySet().parallelStream()
                .forEach(
                        entry -> {
                            ExecutorService executorService = entry.getValue();
                            // 不接受 新任务，等待 现有 任务 执行完毕 后 关闭
                            executorService.shutdown();
                            log.info(
                                    "close threadPool [{}] [{}]",
                                    entry.getKey(),
                                    executorService.isTerminated());
                            try {
                                // 所以 这里 要阻塞 等待 任务 执行完
                                executorService.awaitTermination(10, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                log.error("failed to close thread pool: ", e);
                                // 使用 中断 操作 去尝试 关闭所有 正在执行的 任务
                                executorService.shutdownNow();
                            }
                        });
        log.info("threadPool closed successfully");
    }

    /**
     * 创建 线程池，线程名前缀为 null 时 创建 默认线程工厂,daemon 为 null 时 不设置 守护线程 属性
     *
     * @param threadNamePrefix 线程名 前缀
     * @param daemon 指定 是否为 守护 线程
     * @return ExecutorService
     */
    private static ExecutorService createThreadPool( String threadNamePrefix, Boolean daemon ) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIMEOUT,
                TimeUnit.MINUTES,
                workQueue,
                threadFactory);
    }

    /**
     * 创建 ThreadFactory, 如果 threadNamePrefix 不为空则 使用 ThreadFactory, 否则 默认 创建 defaultThreadFactory
     *
     * @param threadNamePrefix 线程名 前缀
     * @param daemon 指定 是否 为 守护线程
     * @return ThreadFactory
     */
    private static ThreadFactory createThreadFactory( String threadNamePrefix, Boolean daemon ) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon)
                        .build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }
}
