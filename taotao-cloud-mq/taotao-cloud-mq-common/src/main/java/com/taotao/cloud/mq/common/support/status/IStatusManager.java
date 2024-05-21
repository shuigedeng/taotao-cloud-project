package com.taotao.cloud.mq.common.support.status;

/**
 * 状态管理
 * @since 2024.05
 */
public interface IStatusManager {

    /**
     * 获取状态编码
     * @return 状态编码
     * @since 2024.05
     */
    boolean status();

    /**
     * 设置状态编码
     * @param status 编码
     * @return this
     * @since 2024.05
     */
    IStatusManager status(final boolean status);

    /**
     * 初始化失败
     * @return 初始化失败
     * @since 2024.05
     */
    boolean initFailed();

    /**
     * 设置初始化失败
     * @param failed 编码
     * @return this
     * @since 2024.05
     */
    IStatusManager initFailed(final boolean failed);

}
