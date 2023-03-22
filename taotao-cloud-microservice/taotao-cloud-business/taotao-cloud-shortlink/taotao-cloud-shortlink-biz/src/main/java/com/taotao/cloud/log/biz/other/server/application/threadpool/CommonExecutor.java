package com.taotao.cloud.log.biz.other.server.application.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class CommonExecutor {
    @Value("${fixed-pool.thread-count}")
    private int threadCount = 30;

    @Value("${fixed-pool.queue-size}")
    private int queueSize = 100;

    private  BlockingQueue<Runnable> queue =  new LinkedBlockingDeque(queueSize);

    private Executor executorService = new ThreadPoolExecutor(threadCount, threadCount, 600, TimeUnit.SECONDS, queue,
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    log.warn("request for is rejected");
                }
            });

    public void runTask(Runnable task) {
        executorService.execute(task);
    }




}
