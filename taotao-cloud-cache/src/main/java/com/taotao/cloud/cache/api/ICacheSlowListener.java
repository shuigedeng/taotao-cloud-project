package com.taotao.cloud.cache.api;

/**
 * 慢日志操作接口
 *
 * @author shuigedeng
 * @since 0.0.9
 */
public interface ICacheSlowListener {

    /**
     * 监听
     * @param context 上下文
     * @since 0.0.6
     */
    void listen(final ICacheSlowListenerContext context);

    /**
     * 慢日志的阈值
     * @return 慢日志的阈值
     * @since 0.0.9
     */
    long slowerThanMills();

}
