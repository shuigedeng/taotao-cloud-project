package com.taotao.cloud.rpc.common.common.support.delay;

/**
 * 延迟执行器
 *
 * 可以考虑添加对应的销毁任务，暂时不做处理
 *
 * @since 0.1.7
 */
public interface DelayExecutor {

    /**
     * 延迟执行
     * @param delayInMills 延迟执行的毫秒数
     * @param runnable 任务
     */
    void delay(long delayInMills, Runnable runnable);

}
