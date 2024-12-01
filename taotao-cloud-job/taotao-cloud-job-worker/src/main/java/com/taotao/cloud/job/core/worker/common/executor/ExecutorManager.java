package com.taotao.cloud.job.core.worker.common.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;

import java.util.concurrent.*;

public class ExecutorManager
{

    @Getter
    private static  ScheduledExecutorService heartbeatExecutor = null;
    @Getter
    private static  ScheduledExecutorService healthReportExecutor = null;
    @Getter
    private static  ScheduledExecutorService lightweightTaskStatusCheckExecutor = null;
    @Getter
    private static  ExecutorService lightweightTaskExecutorService = null;
    public static void initExecutorManager(){

        final int availableProcessors = Runtime.getRuntime().availableProcessors();

        ThreadFactory heartbeatThreadFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-worker-heartbeat-%d").build();
        heartbeatExecutor =  new ScheduledThreadPoolExecutor(3, heartbeatThreadFactory);

        ThreadFactory healthReportThreadFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-worker-healthReport-%d").build();
        healthReportExecutor =  new ScheduledThreadPoolExecutor(3, healthReportThreadFactory);

        ThreadFactory lightTaskReportFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-worker-light-task-status-check-%d").build();
        lightweightTaskStatusCheckExecutor =  new ScheduledThreadPoolExecutor(availableProcessors * 10, lightTaskReportFactory);

        ThreadFactory lightTaskExecuteFactory = new ThreadFactoryBuilder().setNameFormat("ttcjob-worker-light-task-execute-%d").build();
        lightweightTaskExecutorService = new ThreadPoolExecutor(availableProcessors * 10,availableProcessors * 10, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>((1024 * 2),true), lightTaskExecuteFactory, new ThreadPoolExecutor.AbortPolicy());

    }

    public static void shutdown(){
        heartbeatExecutor.shutdownNow();
        lightweightTaskExecutorService.shutdown();
        lightweightTaskStatusCheckExecutor.shutdown();
    }


}
