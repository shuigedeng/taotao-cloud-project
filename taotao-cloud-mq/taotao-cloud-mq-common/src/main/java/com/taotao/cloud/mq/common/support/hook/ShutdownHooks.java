package com.taotao.cloud.mq.common.support.hook;

/**
 * @since 2024.05
 */
public final class ShutdownHooks {

    private ShutdownHooks(){}

    /**
     * 添加 rpc shutdown hook
     * @param rpcShutdownHook 钩子函数实现
     * @since 2024.05
     */
    public static void rpcShutdownHook(final RpcShutdownHook rpcShutdownHook) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                rpcShutdownHook.hook();
            }
        });
    }

}
