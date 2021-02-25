package com.taotao.cloud.java.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * 列出并发包中的各种线程池
 *
 * @author
 */

public class ExecutorDemo {

    public static void main(String[] args) {
        ExecutorService newSingleThreadExecutor = newSingleThreadExecutor();
        ExecutorService newCachedThreadPool = newCachedThreadPool();

        int cpuNums = Runtime.getRuntime().availableProcessors();

        System.out.println(cpuNums);
        ExecutorService newFixedThreadPool = newFixedThreadPool(cpuNums);
        ScheduledExecutorService newScheduledThreadPool = newScheduledThreadPool(8);


        ScheduledExecutorService newsinglethreadscheduledexecutor;
        newsinglethreadscheduledexecutor = newSingleThreadScheduledExecutor();
    }
}
