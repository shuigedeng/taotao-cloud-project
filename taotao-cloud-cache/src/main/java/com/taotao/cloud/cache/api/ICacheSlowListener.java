package com.taotao.cloud.cache.api;

/**
 * 慢日志操作接口
 *
 * @author shuigedeng
 * @since 2024.06
 */
public interface ICacheSlowListener {

    /**
     * 监听
     * @param context 上下文
     * @since 2024.06
     */
    void listen(final ICacheSlowListenerContext context);

    /**
     * 慢日志的阈值
     * @return 慢日志的阈值
     * @since 2024.06
     */
    long slowerThanMills();

}
