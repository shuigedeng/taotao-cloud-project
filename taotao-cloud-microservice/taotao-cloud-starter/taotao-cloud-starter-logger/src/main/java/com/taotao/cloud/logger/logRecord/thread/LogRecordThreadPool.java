package com.taotao.cloud.logger.logRecord.thread;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.logRecord.configuration.LogRecordProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
@EnableConfigurationProperties(value = LogRecordProperties.class)
public class LogRecordThreadPool {

    private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory("log-record-");

    private final ExecutorService LOG_RECORD_POOL_EXECUTOR;

    public LogRecordThreadPool(LogRecordProperties logRecordProperties){
        LogUtil.info("LogRecordThreadPool init poolSize [{}]", logRecordProperties.getPoolSize());
        int poolSize = logRecordProperties.getPoolSize();
        this.LOG_RECORD_POOL_EXECUTOR = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ExecutorService getLogRecordPoolExecutor() {
        return LOG_RECORD_POOL_EXECUTOR;
    }
}
