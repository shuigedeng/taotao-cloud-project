package com.taotao.cloud.message.biz.austin.support.utils;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.taotao.cloud.message.biz.austin.support.config.ThreadPoolExecutorShutdownDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 线程池工具类
 *
 * @author shuigedeng
 */
@Component
public class ThreadPoolUtils {

    private static final String SOURCE_NAME = "austin";
    @Autowired
    private ThreadPoolExecutorShutdownDefinition shutdownDefinition;

    /**
     * 1. 将当前线程池 加入到 动态线程池内
     * 2. 注册 线程池 被Spring管理，优雅关闭
     */
    public void register(DtpExecutor dtpExecutor) {
        DtpRegistry.register(dtpExecutor, SOURCE_NAME);
        shutdownDefinition.registryExecutor(dtpExecutor);
    }
}
