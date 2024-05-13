package com.taotao.cloud.rpc.common.common.support.hook;

/**
 * rpc 关闭 hook
 * （1）可以添加对应的 hook 管理类
 * @since 0.1.3
 */
public interface RpcShutdownHook {

    /**
     * 钩子函数实现
     * @since 0.1.3
     */
    void hook();

}
