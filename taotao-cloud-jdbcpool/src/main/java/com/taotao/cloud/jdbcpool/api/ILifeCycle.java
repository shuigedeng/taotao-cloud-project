package com.github.houbb.thread.pool.api;

/**
 * 生命周期管理
 * @since 1.1.0
 */
public interface ILifeCycle {

    /**
     * 生命的初始化
     * @since 1.1.0
     */
    void init();

    /**
     * 生命的销毁
     * @since 1.1.0
     */
    void destroy();

}
