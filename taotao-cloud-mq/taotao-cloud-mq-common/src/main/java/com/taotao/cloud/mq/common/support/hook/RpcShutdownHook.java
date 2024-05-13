package com.taotao.cloud.mq.common.support.hook;

/**
 * rpc 关闭 hook
 * （1）可以添加对应的 hook 管理类
 * @since 2024.05
 */
public interface RpcShutdownHook {

    /**
     * 钩子函数实现
     * @since 2024.05
     */
    void hook();

}
